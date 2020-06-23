package com.wyy.actable.service;

import com.wyy.actable.annotation.Column;
import com.wyy.actable.annotation.Index;
import com.wyy.actable.annotation.Table;
import com.wyy.actable.annotation.Unique;
import com.wyy.actable.command.CodeColumn;
import com.wyy.actable.command.DBColumn;
import com.wyy.actable.constants.Constants;
import com.wyy.actable.executor.executors.*;
import com.wyy.actable.utils.ClassTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Date: 20-2-6
 * @Author: wyy
 */
@Component
public class SysMysqlCreateTableServiceImpl implements SysMysqlCreateTableService {

    private static final Logger log = LoggerFactory.getLogger(SysMysqlCreateTableServiceImpl.class);

    @Autowired
    private OldTableExecutor oldTableExecutor;
    @Autowired
    private CreateTableExecutor createTableExecutor;
    @Autowired
    private AddIndexExecutor addIndexExecutor;
    @Autowired
    private AddUniqueExecutor addUniqueExecutor;
    @Autowired
    private AddColumnExecutor addColumnExecutor;
    @Autowired
    private DeleteColumnExecutor deleteColumnExecutor;
    @Autowired
    private ModifyColumnExecutor modifyColumnExecutor;
    @Autowired
    private DropIndexExecutor dropIndexExecutor;

    @Value(Constants.MODEL_PACK_KEY_VALUE)
    private String pack;

    /**
     * 自动创建模式：update表示更新，create表示删除原表重新创建
     */
    @Value(Constants.CREATE_MODE_KEY_VALUE)
    private String createTableMode;

    @PostConstruct
    public void init() throws SQLException {
        createMysqlTable();
    }

    /**
     * 读取配置文件的三种状态（创建表、更新表、不做任何事情）
     */
    public void createMysqlTable() throws SQLException {
        // 读取配置信息
//        pack = springContextUtil.getConfig(Constants.MODEL_PACK_KEY);
//        createTableMode = springContextUtil.getConfig(Constants.CREATE_MODE_KEY);

        // 不做任何事情
        if (!"none".equals(createTableMode) && !"update".equals(createTableMode) && !"create".equals(createTableMode)) {
            log.warn("配置mybatis.table.auto错误无法识别，当前配置只支持[none/update/create]三种类型!");
            return;
        }

        // 不做任何事情
        if ("none".equals(createTableMode)) {
            log.info("配置mybatis.table.auto=none，不需要做任何事情");
            return;
        }

        // 从包package中获取所有的Class
        Set<Class<?>> classes = ClassTools.getClasses(pack);

        // 循环全部的model
        for (Class<?> clazz : classes) {

            // 没有打注解不需要创建表
            if (null == clazz.getAnnotation(Table.class)) {
                continue;
            }
            // 构建出全部表的增删改的map
            analyzeModel(clazz);
        }
    }

    /**
     * 构建出全部表的增删改的map
     *
     * @param clazz package中的model的Class
     */
    private void analyzeModel(Class<?> clazz) throws SQLException {

        // 获取model的table注解
        Table table = clazz.getAnnotation(Table.class);

        // 1. 用于存表的全部字段
        List<CodeColumn> codeColumnList = getAllFields(clazz);
        if (codeColumnList.size() == 0) {
            log.warn("扫描model发现" + clazz.getName() + "没有建表字段请检查！");
            return;
        }

        // 如果配置文件配置的是create，表示将所有的表删掉重新创建
        String tableName = table.name();
        if ("create".equals(createTableMode)) {
            oldTableExecutor.dropTable(tableName);
        }

        // 先查该表是否以存在
        boolean exist = oldTableExecutor.checkTableExist(tableName);

        // 不存在时
        if (!exist) {
            createTableExecutor.create(tableName, codeColumnList);
            addIndexExecutor.add(tableName, new HashSet<>(codeColumnList));
            addUniqueExecutor.add(tableName, new HashSet<>(codeColumnList));
            return;
        }

        // 已存在时理论上做修改的操作，这里查出该表的结构
        List<DBColumn> tableColumnList = oldTableExecutor.findTableEnsembleByTableName(tableName);

        // 从sysColumns中取出我们需要比较的列的List
        // 先取出name用来筛选出增加和删除的字段
        List<String> dbColumnNames = tableColumnList.stream().map(DBColumn::getColumn_name).collect(Collectors.toList());
        List<String> codeColumnNames = codeColumnList.stream().map(CodeColumn::getName).collect(Collectors.toList());

        // 验证对比从model中解析的allFieldList与从数据库查出来的columnList
        // 2. 找出增加的字段
        List<CodeColumn> needAddColumnList = codeColumnList.stream().filter(codeColumn -> !dbColumnNames.contains(codeColumn.getName())).collect(Collectors.toList());
        addColumnExecutor.add(tableName, needAddColumnList);

        // 3. 找出删除的字段
        List<String> needDeleteColumnList = dbColumnNames.stream().filter(it -> !codeColumnNames.contains(it)).collect(Collectors.toList());
        deleteColumnExecutor.delete(tableName, needDeleteColumnList);
        // 4. 找出更新的字段
        List<CodeColumn> modifyFieldList = getModifyFieldList(tableColumnList, codeColumnList);
        modifyColumnExecutor.modify(tableName, modifyFieldList);

        // 5. 查询当前表中全部的索引和唯一约束
        Set<String> dbIndexAndUniqueNames = oldTableExecutor.findTableIndexByTableName(tableName);

        // 6. 找出需要删除的索引和唯一约束
        Set<String> dropIndexAndUniqueFieldSet = getDropIndexAndUniqueList(dbIndexAndUniqueNames, codeColumnList);
        dropIndexExecutor.drop(tableName, dropIndexAndUniqueFieldSet);
        // 7. 找出需要新增的索引
        Set<CodeColumn> addIndexFieldSet = getAddIndexColumn(dbIndexAndUniqueNames, codeColumnList);
        addIndexExecutor.add(tableName, addIndexFieldSet);
        // 8. 找出需要新增的唯一约束
        Set<CodeColumn> addUniqueFieldSet = getAddUniqueSet(dbIndexAndUniqueNames, codeColumnList);
        addUniqueExecutor.add(tableName, addUniqueFieldSet);
    }

    /**
     * 找出需要新建的索引
     *
     * @param dbIndexAndUniqueNameSet 当前数据库的索引很约束名
     * @param codeColumnList          model中的所有字段
     * @return 需要新建的索引
     */
    private Set<CodeColumn> getAddIndexColumn(Set<String> dbIndexAndUniqueNameSet, List<CodeColumn> codeColumnList) {
        Set<CodeColumn> codeColumnSet = new HashSet<>();
        if (null == dbIndexAndUniqueNameSet) {
            dbIndexAndUniqueNameSet = new HashSet<>();
        }

        for (CodeColumn codeColumn : codeColumnList) {
            if (codeColumn.getIndexName() != null && !dbIndexAndUniqueNameSet.contains(codeColumn.getIndexName())) {
                codeColumnSet.add(codeColumn);
            }
        }
        return codeColumnSet;
    }

    /**
     * 找出需要新建的唯一约束
     *
     * @param dbIndexAndUniqueNameSet 当前数据库的索引很约束名
     * @param codeColumnList          model中的所有字段
     * @return 需要新建的唯一约束
     */
    private Set<CodeColumn> getAddUniqueSet(Set<String> dbIndexAndUniqueNameSet, List<CodeColumn> codeColumnList) {
        Set<CodeColumn> codeColumnSet = new HashSet<>();
        if (null == dbIndexAndUniqueNameSet) {
            dbIndexAndUniqueNameSet = new HashSet<>();
        }

        for (CodeColumn codeColumn : codeColumnList) {
            if (codeColumn.getUniqueName() != null && !dbIndexAndUniqueNameSet.contains(codeColumn.getUniqueName())) {
                codeColumnSet.add(codeColumn);
            }
        }

        return codeColumnSet;
    }

    /**
     * 找出需要删除的索引和唯一约束
     *
     * @param allIndexAndUniqueNameSet 当前数据库的索引很约束名
     * @param codeColumnList           model中的所有字段
     * @return 需要删除的索引和唯一约束
     */
    private Set<String> getDropIndexAndUniqueList(Set<String> allIndexAndUniqueNameSet, List<CodeColumn> codeColumnList) {
        Set<String> dropIndexAndUniqueFieldList = new HashSet<>();
        if (null == allIndexAndUniqueNameSet || allIndexAndUniqueNameSet.size() == 0) {
            return dropIndexAndUniqueFieldList;
        }
        Set<CodeColumn> indexAndUniqueColumnSet = codeColumnList.stream().filter(codeColumn -> codeColumn.getIndexName() != null || codeColumn.getUniqueName() != null).collect(Collectors.toSet());
        Set<String> indexAndUniqueNameSet = new HashSet<>();
        indexAndUniqueNameSet.addAll(indexAndUniqueColumnSet.stream().map(CodeColumn::getIndexName).collect(Collectors.toSet()));
        indexAndUniqueNameSet.addAll(indexAndUniqueColumnSet.stream().map(CodeColumn::getUniqueName).collect(Collectors.toSet()));

        return allIndexAndUniqueNameSet.stream().filter(name -> !indexAndUniqueNameSet.contains(name)).collect(Collectors.toSet());
    }

    /**
     * 根据数据库中表的结构和model中表的结构对比找出修改类型默认值等属性的字段
     *
     * @param tableColumnList 表结构
     * @param codeColumnList  model中的所有字段
     * @return 需要修改的字段
     */
    private List<CodeColumn> getModifyFieldList(List<DBColumn> tableColumnList, List<CodeColumn> codeColumnList) {

        Set<String> dbColumnNameSet = tableColumnList.stream().map(DBColumn::getColumn_name).collect(Collectors.toSet());

        List<CodeColumn> modifyFieldList = new ArrayList<>();
        Map<String, CodeColumn> name_codeColumn_map = codeColumnList.stream().filter(codeColumn -> dbColumnNameSet.contains(codeColumn.getName()))
                .collect(Collectors.toMap(CodeColumn::getName, it -> it));
        //过滤出代码中有的列
        for (DBColumn dbColumn : tableColumnList) {
            // 数据库中有该字段时，验证是否有更新
            CodeColumn codeColumn = name_codeColumn_map.get(dbColumn.getColumn_name());

            if (codeColumn != null) {
                // 1.验证主键
                // 原本不是主键，现在变成了主键，那么要去做更新
                if (!"PRI".equals(dbColumn.getColumn_key()) && codeColumn.isKey()) {
                    modifyFieldList.add(codeColumn);
                    continue;
                }
                // 原本是主键，现在依然主键，坚决不能在alter语句后加primary key，否则会报multiple primary
                // key defined
                if ("PRI".equals(dbColumn.getColumn_key()) && codeColumn.isKey()) {
                    codeColumn.setKey(false);
                }
                // 2.验证类型
                if (!dbColumn.getData_type().toLowerCase().equals(codeColumn.getType().toLowerCase())) {
                    modifyFieldList.add(codeColumn);
                    continue;
                }
                // 3.验证长度个小数点位数
                // 4.验证小数点位数
                String typeAndLength = codeColumn.getType();
                if (codeColumn.getDecimal() > 0) {
                    // 拼接出类型加长度，比如varchar(1)
                    typeAndLength = typeAndLength + "(" + codeColumn.getLength() + ","
                            + codeColumn.getDecimal() + ")";
                } else if (codeColumn.getLength() > 0) {
                    // 拼接出类型加长度，比如varchar(1)
                    typeAndLength = typeAndLength + "(" + codeColumn.getLength() + ")";
                }

                // 判断类型+长度是否相同
                if (!dbColumn.getColumn_type().toLowerCase().equals(typeAndLength)) {
                    modifyFieldList.add(codeColumn);
                    continue;
                }
                // 5.验证自增
                if ("auto_increment".equals(dbColumn.getExtra()) && !codeColumn.isAutoIncrement()) {
                    modifyFieldList.add(codeColumn);
                    continue;
                }
                // 6.验证默认值
                if (dbColumn.getColumn_default() == null || dbColumn.getColumn_default().equals("")) {
                    // 数据库默认值是null，model中注解设置的默认值不为NULL时，那么需要更新该字段
                    if (codeColumn.getDefaultValue() != null && !"NULL".equalsIgnoreCase(codeColumn.getDefaultValue())) {
                        modifyFieldList.add(codeColumn);
                        continue;
                    }
                } else if (!dbColumn.getColumn_default().equals(codeColumn.getDefaultValue())) {
                    // 两者不相等时，需要更新该字段
                    modifyFieldList.add(codeColumn);
                    continue;
                }
                // 7.验证是否可以为null(主键不参与是否为null的更新)
                if (dbColumn.getIs_nullable().equals("NO") && !codeColumn.isKey()) {
                    if (codeColumn.isNullable()) {
                        // 一个是可以一个是不可用，所以需要更新该字段
                        modifyFieldList.add(codeColumn);
                        continue;
                    }
                } else if (dbColumn.getIs_nullable().equals("YES") && !codeColumn.isKey()) {
                    if (!codeColumn.isNullable()) {
                        // 一个是可以一个是不可用，所以需要更新该字段
                        modifyFieldList.add(codeColumn);
                        continue;
                    }
                }
                // 8.验证注释
                if (!dbColumn.getColumn_comment().equals(codeColumn.getComment())) {
                    modifyFieldList.add(codeColumn);
                }
            }
        }
        return modifyFieldList;
    }

    /**
     * 根据实体类，解析出所有的属性信息，包含父类
     *
     * @param clazz 需要建表的实体类
     * @return 表的全部字段
     */
    private List<CodeColumn> getAllFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();

        List<CodeColumn> codeColumnList = new ArrayList<>();

        // 判断是否有父类，如果有拉取父类的field，这里只支持多层继承
        fields = recursionParents(clazz, fields);

        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                Index index = field.getAnnotation(Index.class);
                Unique unique = field.getAnnotation(Unique.class);

                codeColumnList.add(new CodeColumn(column, index, unique));
            }
        }
        return codeColumnList;
    }

    /**
     * 递归扫描父类的fields
     *
     * @param clazz  类
     * @param fields 属性
     */
    @SuppressWarnings("rawtypes")
    private Field[] recursionParents(Class<?> clazz, Field[] fields) {
        if (clazz.getSuperclass() != null) {
            Class clsSup = clazz.getSuperclass();
            List<Field> fieldList = new ArrayList<Field>();
            fieldList.addAll(Arrays.asList(fields));
            fieldList.addAll(Arrays.asList(clsSup.getDeclaredFields()));
            fields = new Field[fieldList.size()];
            int i = 0;
            for (Object field : fieldList.toArray()) {
                fields[i] = (Field) field;
                i++;
            }
            fields = recursionParents(clsSup, fields);
        }
        return fields;
    }
}

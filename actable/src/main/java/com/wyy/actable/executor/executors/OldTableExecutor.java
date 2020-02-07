package com.wyy.actable.executor.executors;

import com.wyy.actable.command.DBColumn;
import com.wyy.actable.executor.MySqlExecutor;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Date: 20-2-6
 * @Author: wyy
 */

/**
 * 建表，添加索引，添加unique
 */
@Component
public class OldTableExecutor extends MySqlExecutor {

    public boolean checkTableExist(String tableName) throws SQLException {
        String sql = "select count(1) from information_schema.tables where table_name = '" + tableName + "' and table_schema = (select database())";
        final boolean[] exist = new boolean[1];
        execute(sql, resultSet -> {
            while (resultSet.next()) {
                exist[0] = resultSet.getInt(1) > 0;
            }
        });

        return exist[0];
    }

    public List<DBColumn> findTableEnsembleByTableName(String tableName) throws SQLException {
        String sql = "select * from information_schema.columns where table_name = '" + tableName + "' and table_schema = (select database())";
        List<DBColumn> dbColumnList = new ArrayList<>();
        execute(sql, resultSet -> {
            while (resultSet.next()) {
                String table_catalog = resultSet.getString("table_catalog");
                String table_name = resultSet.getString("table_name");
                String column_name = resultSet.getString("column_name");
                String ordinal_position = resultSet.getString("ordinal_position");
                String column_default = resultSet.getString("column_default");
                String is_nullable = resultSet.getString("is_nullable");
                String data_type = resultSet.getString("data_type");
                String character_maximum_length = resultSet.getString("character_maximum_length");
                String character_octet_length = resultSet.getString("character_octet_length");
                String numeric_precision = resultSet.getString("numeric_precision");
                String numeric_scale = resultSet.getString("numeric_scale");
                String character_set_name = resultSet.getString("character_set_name");
                String collation_name = resultSet.getString("collation_name");
                String column_type = resultSet.getString("column_type");
                String column_key = resultSet.getString("column_key");
                String extra = resultSet.getString("extra");
                String privileges = resultSet.getString("privileges");
                String column_comment = resultSet.getString("column_comment");

                DBColumn dbColumn = new DBColumn()
                        .setTable_catalog(table_catalog)
                        .setTable_name(table_name)
                        .setColumn_name(column_name)
                        .setOrdinal_position(ordinal_position)
                        .setColumn_default(column_default)
                        .setIs_nullable(is_nullable)
                        .setData_type(data_type)
                        .setCharacter_maximum_length(character_maximum_length)
                        .setCharacter_octet_length(character_octet_length)
                        .setNumeric_precision(numeric_precision)
                        .setNumeric_scale(numeric_scale)
                        .setCharacter_set_name(character_set_name)
                        .setCollation_name(collation_name)
                        .setColumn_type(column_type)
                        .setColumn_type(column_type)
                        .setColumn_key(column_key)
                        .setExtra(extra)
                        .setPrivileges(privileges)
                        .setColumn_comment(column_comment);

                dbColumnList.add(dbColumn);
            }
        });
        return dbColumnList;
    }

    /**
     * 查询当前表存在的索引除了主键索引primary
     *
     * @param tableName
     * @return
     */
    public Set<String> findTableIndexByTableName(String tableName) throws SQLException {
        String sql = "select index_name from information_schema.statistics where table_name = '" + tableName + "' and table_schema = (select database()) and lower(index_name) !='primary'";
        Set<String> indexNameSet = new HashSet<>();
        execute(sql, resultSet -> {
            while (resultSet.next()) {
                indexNameSet.add(resultSet.getString(1));
            }
        });
        return indexNameSet;
    }

    public void dropTable(String tableName) throws SQLException {
        String sql = "drop table if exists " + tableName + ";";
        execute(sql);
    }
}

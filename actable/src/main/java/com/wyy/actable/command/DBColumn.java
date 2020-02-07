package com.wyy.actable.command;

/**
 * @Date: 20-2-6
 * @Author: wyy
 */
public class DBColumn {

    /**
     * 字段名
     */
    public static final String COLUMN_NAME_KEY = "column_name";
    /**
     * 默认值
     */
    public static final String COLUMN_DEFAULT_KEY = "column_default";
    /**
     * 是否可为null，值：(YES,NO)
     */
    public static final String IS_NULLABLE_KEY = "is_nullable";
    /**
     * 数据类型
     */
    public static final String DATA_TYPE_KEY = "data_type";
    /**
     * 长度，如果是0的话是null
     */
    public static final String NUMERIC_PRECISION_KEY = "numeric_precision";
    /**
     * 小数点数
     */
    public static final String NUMERIC_SCALE_KEY = "numeric_scale";
    /**
     * 是否为主键，是的话是PRI
     */
    public static final String COLUMN_KEY_KEY = "column_key";
    /**
     * 是否为自动增长，是的话为auto_increment
     */
    public static final String EXTRA_KEY = "extra";

    private String table_catalog;
    /**
     * 库名
     */
    private String table_schema;
    /**
     * 表名
     */
    private String table_name;
    /**
     * 字段名
     */
    private String column_name;
    /**
     * 字段位置的排序
     */
    private String ordinal_position;
    /**
     * 字段默认值
     */
    private String column_default;
    /**
     * 是否可以为null
     */
    private String is_nullable;
    /**
     * 字段类型
     */
    private String data_type;
    private String character_maximum_length;
    private String character_octet_length;
    /**
     * 长度
     */
    private String numeric_precision;
    /**
     * 小数点数
     */
    private String numeric_scale;
    private String character_set_name;
    private String collation_name;
    /**
     * 类型加长度拼接的字符串，例如varchar(100)
     */
    private String column_type;
    /**
     * 主键:PRI；唯一键:UNI
     */
    private String column_key;
    /**
     * 是否为自动增长，是的话为auto_increment
     */
    private String extra;
    private String privileges;
    private String column_comment;

    public static String getColumnNameKey() {
        return COLUMN_NAME_KEY;
    }

    public static String getColumnDefaultKey() {
        return COLUMN_DEFAULT_KEY;
    }

    public static String getIsNullableKey() {
        return IS_NULLABLE_KEY;
    }

    public static String getDataTypeKey() {
        return DATA_TYPE_KEY;
    }

    public static String getNumericPrecisionKey() {
        return NUMERIC_PRECISION_KEY;
    }

    public static String getNumericScaleKey() {
        return NUMERIC_SCALE_KEY;
    }

    public static String getColumnKeyKey() {
        return COLUMN_KEY_KEY;
    }

    public static String getExtraKey() {
        return EXTRA_KEY;
    }

    public String getTable_catalog() {
        return table_catalog;
    }

    public DBColumn setTable_catalog(String table_catalog) {
        this.table_catalog = table_catalog;
        return this;
    }

    public String getTable_schema() {
        return table_schema;
    }

    public DBColumn setTable_schema(String table_schema) {
        this.table_schema = table_schema;
        return this;
    }

    public String getTable_name() {
        return table_name;
    }

    public DBColumn setTable_name(String table_name) {
        this.table_name = table_name;
        return this;
    }

    public String getColumn_name() {
        return column_name;
    }

    public DBColumn setColumn_name(String column_name) {
        this.column_name = column_name;
        return this;
    }

    public String getOrdinal_position() {
        return ordinal_position;
    }

    public DBColumn setOrdinal_position(String ordinal_position) {
        this.ordinal_position = ordinal_position;
        return this;
    }

    public String getColumn_default() {
        return column_default;
    }

    public DBColumn setColumn_default(String column_default) {
        this.column_default = column_default;
        return this;
    }

    public String getIs_nullable() {
        return is_nullable;
    }

    public DBColumn setIs_nullable(String is_nullable) {
        this.is_nullable = is_nullable;
        return this;
    }

    public String getData_type() {
        return data_type;
    }

    public DBColumn setData_type(String data_type) {
        this.data_type = data_type;
        return this;
    }

    public String getCharacter_maximum_length() {
        return character_maximum_length;
    }

    public DBColumn setCharacter_maximum_length(String character_maximum_length) {
        this.character_maximum_length = character_maximum_length;
        return this;
    }

    public String getCharacter_octet_length() {
        return character_octet_length;
    }

    public DBColumn setCharacter_octet_length(String character_octet_length) {
        this.character_octet_length = character_octet_length;
        return this;
    }

    public String getNumeric_precision() {
        return numeric_precision;
    }

    public DBColumn setNumeric_precision(String numeric_precision) {
        this.numeric_precision = numeric_precision;
        return this;
    }

    public String getNumeric_scale() {
        return numeric_scale;
    }

    public DBColumn setNumeric_scale(String numeric_scale) {
        this.numeric_scale = numeric_scale;
        return this;
    }

    public String getCharacter_set_name() {
        return character_set_name;
    }

    public DBColumn setCharacter_set_name(String character_set_name) {
        this.character_set_name = character_set_name;
        return this;
    }

    public String getCollation_name() {
        return collation_name;
    }

    public DBColumn setCollation_name(String collation_name) {
        this.collation_name = collation_name;
        return this;
    }

    public String getColumn_type() {
        return column_type;
    }

    public DBColumn setColumn_type(String column_type) {
        this.column_type = column_type;
        return this;
    }

    public String getColumn_key() {
        return column_key;
    }

    public DBColumn setColumn_key(String column_key) {
        this.column_key = column_key;
        return this;
    }

    public String getExtra() {
        return extra;
    }

    public DBColumn setExtra(String extra) {
        this.extra = extra;
        return this;
    }

    public String getPrivileges() {
        return privileges;
    }

    public DBColumn setPrivileges(String privileges) {
        this.privileges = privileges;
        return this;
    }

    public String getColumn_comment() {
        return column_comment;
    }

    public DBColumn setColumn_comment(String column_comment) {
        this.column_comment = column_comment;
        return this;
    }
}

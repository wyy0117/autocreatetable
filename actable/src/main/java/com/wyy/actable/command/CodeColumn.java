package com.wyy.actable.command;

import com.wyy.actable.annotation.Column;
import com.wyy.actable.annotation.Index;
import com.wyy.actable.annotation.Unique;
import com.wyy.actable.constants.Constants;

/**
 * @Date: 20-2-6
 * @Author: wyy
 */
public class CodeColumn {

    private String name;
    private String type;
    private int length;
    private int decimal;
    private boolean nullable;
    private boolean key;
    private boolean autoIncrement;
    private String defaultValue;
    private String comment;
    private String uniqueName;
    private String indexName;

    public CodeColumn(Column column, Index index, Unique unique) {
        this.name = column.name();
        this.type = column.type();
        this.length = column.length();
        this.decimal = column.decimal();
        this.nullable = column.nullable();
        this.key = column.key();
        this.autoIncrement = column.autoIncrement();
        if (!column.defaultValue().equalsIgnoreCase("null")) {
            this.defaultValue = column.defaultValue();
        }
        this.comment = column.comment();
        if (index != null) {
            if (!index.name().equals("")) {
                this.indexName = index.name();
            } else {
                this.indexName = Constants.IDX + column.name();
            }
        }
        if (unique != null) {
            if (!unique.name().equals("")) {
                this.uniqueName = unique.name();
            } else {
                this.uniqueName = Constants.UNI + column.name();
            }
        }
    }

    public String dbType() {

        StringBuilder stringBuilder = new StringBuilder(this.type);
        if (this.decimal > 0) {
            stringBuilder.append("(");
            stringBuilder.append(this.length);
            stringBuilder.append(",");
            stringBuilder.append(this.decimal);
            stringBuilder.append(")");
        } else if (this.length > 0) {
            stringBuilder.append("(");
            stringBuilder.append(this.length);
            stringBuilder.append(")");
        }
        return stringBuilder.toString();
    }


    public String getUniqueName() {
        return uniqueName;
    }

    public CodeColumn setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
        return this;
    }

    public String getIndexName() {
        return indexName;
    }

    public CodeColumn setIndexName(String indexName) {
        this.indexName = indexName;
        return this;
    }

    public String getName() {
        return name;
    }

    public CodeColumn setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public CodeColumn setType(String type) {
        this.type = type;
        return this;
    }

    public int getLength() {
        return length;
    }

    public CodeColumn setLength(int length) {
        this.length = length;
        return this;
    }

    public int getDecimal() {
        return decimal;
    }

    public CodeColumn setDecimal(int decimal) {
        this.decimal = decimal;
        return this;
    }

    public boolean isNullable() {
        return nullable;
    }

    public CodeColumn setNullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public boolean isKey() {
        return key;
    }

    public CodeColumn setKey(boolean key) {
        this.key = key;
        return this;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public CodeColumn setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
        return this;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public CodeColumn setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public CodeColumn setComment(String comment) {
        this.comment = comment;
        return this;
    }
}

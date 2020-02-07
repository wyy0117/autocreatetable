package com.wyy.actable.executor;

import com.wyy.actable.command.CodeColumn;
import com.wyy.actable.constants.Constants;
import com.wyy.actable.constants.MySqlDataType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @Date: 20-2-6
 * @Author: wyy
 */
public abstract class MySqlExecutor implements SqlExecutor {

    private Connection connection = null;

    @Value(Constants.EXECUTE_SQL_KEY_VALUE)
    private boolean executeSql;

    @Autowired
    private MysqlConnectionFactory mysqlConnectionFactory;

    @PostConstruct
    public void init() throws SQLException {
        this.connection = mysqlConnectionFactory.getConnection();
        this.connection.setAutoCommit(false);
    }

    @Override
    public void execute(String sql) throws SQLException {
        for (int i = 0; i < 5; i++) {
            System.out.println();
        }
        System.out.println(sql);
        for (int i = 0; i < 5; i++) {
            System.out.println();
        }
        if (!executeSql) {
            return;
        }
        Statement statement = this.connection.prepareStatement(sql);
        statement.execute(sql);
        connection.commit();
        statement.close();
    }

    @Override
    public void execute(String sql, MySqlResultSetHandler resultSetHandler) throws SQLException {
        for (int i = 0; i < 5; i++) {
            System.out.println();
        }
        System.out.println(sql);
        for (int i = 0; i < 5; i++) {
            System.out.println();
        }
        Statement statement = this.connection.prepareStatement(sql);

        ResultSet resultSet = statement.executeQuery(sql);
        this.connection.commit();
        resultSetHandler.handle(resultSet);
        resultSet.close();
        statement.close();
    }

    protected StringBuilder appendColumn(List<CodeColumn> codeColumnList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < codeColumnList.size(); i++) {
            CodeColumn codeColumn = codeColumnList.get(i);
            sb.append(appendColumn(codeColumn));

            if (i != codeColumnList.size() - 1) {
                sb.append(",\n");
            } else {
                sb.append("\n");
            }
        }
        return sb;
    }

    /**
     * 不包含逗号
     *
     * @param codeColumn
     * @return
     */
    protected StringBuilder appendColumn(CodeColumn codeColumn) {
        StringBuilder sb = new StringBuilder();
        sb.append(codeColumn.getName());
        sb.append(" ");
        sb.append(codeColumn.dbType());
        sb.append(" ");

        if (codeColumn.isKey()) {
            sb.append("primary key ");
        }

        if (codeColumn.isNullable()) {
            sb.append("null ");
        } else {
            sb.append("not null ");
        }
        if (codeColumn.isAutoIncrement()) {
            sb.append("auto_increment ");
        }

        if (!codeColumn.isAutoIncrement() && codeColumn.getDefaultValue() != null && !codeColumn.isKey()) {
            if (codeColumn.getType().equals(MySqlDataType.BIT)) {
                if (codeColumn.getDefaultValue().equals("1") || codeColumn.getDefaultValue().equals("true")) {
                    sb.append("default 1");
                } else if (codeColumn.getDefaultValue().equals("0") || codeColumn.getDefaultValue().equals("false")) {
                    sb.append("default 0");
                }
            } else {
                sb.append("default '");
                sb.append(codeColumn.getDefaultValue());
                sb.append("'");
            }
        }

        if (!codeColumn.getComment().equals("") && codeColumn.getComment() != null) {
            sb.append(" comment '");
            sb.append(codeColumn.getComment());
            sb.append("'");
        }
        return sb;
    }
}

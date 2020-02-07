package com.wyy.actable.executor;


import java.sql.SQLException;

/**
 * @Date: 20-2-6
 * @Author: wyy
 */
public interface SqlExecutor {

    void execute(String sql) throws SQLException;

    void execute(String sql, MySqlResultSetHandler resultSetHandler) throws SQLException;

}

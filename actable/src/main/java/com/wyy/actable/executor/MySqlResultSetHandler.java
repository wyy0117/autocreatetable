package com.wyy.actable.executor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Date: 20-2-6
 * @Author: wyy
 */
public interface MySqlResultSetHandler {
    public void handle(ResultSet resultSet) throws SQLException;
}

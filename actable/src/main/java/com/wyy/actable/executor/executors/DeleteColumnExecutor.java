package com.wyy.actable.executor.executors;

import com.wyy.actable.executor.MySqlExecutor;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * @Date: 20-2-6
 * @Author: wyy
 */
@Component
public class DeleteColumnExecutor extends MySqlExecutor {
    public void delete(String tableName, List<String> columnNameList) throws SQLException {
        if (columnNameList.size() == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        columnNameList.forEach(name -> {
            sb.append("alter table ");
            sb.append(tableName);
            sb.append(" drop ");
            sb.append(name);
            sb.append(";\n");
        });
        execute(sb.toString());
    }
}

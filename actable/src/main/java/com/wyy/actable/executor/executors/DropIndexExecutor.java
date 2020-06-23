package com.wyy.actable.executor.executors;

import com.wyy.actable.executor.MySqlExecutor;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Set;

/**
 * @Date: 20-2-6
 * @Author: wyy
 */
@Component
public class DropIndexExecutor extends MySqlExecutor {

    public void drop(String tableName, Set<String> indexNameSet) throws SQLException {
        if (indexNameSet.size() == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        indexNameSet.forEach(name -> {
            sb.append("drop index ");
            sb.append(name);
            sb.append(" on ");
            sb.append(tableName);
            sb.append(";\n");
        });
        execute(sb.toString());
    }
}

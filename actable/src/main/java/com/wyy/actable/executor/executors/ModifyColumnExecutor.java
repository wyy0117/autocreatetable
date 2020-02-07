package com.wyy.actable.executor.executors;

import com.wyy.actable.command.CodeColumn;
import com.wyy.actable.executor.MySqlExecutor;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * @Date: 20-2-6
 * @Author: wyy
 */
@Component
public class ModifyColumnExecutor extends MySqlExecutor {

    public void modify(String tableName, List<CodeColumn> codeColumnList) throws SQLException {
        if (codeColumnList.size() == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        codeColumnList.forEach(codeColumn -> {
            sb.append("alter table ");
            sb.append(tableName);
            sb.append(" modify ");
            sb.append(appendColumn(codeColumn));
            sb.append(";\n");
        });
        execute(sb.toString());
    }
}

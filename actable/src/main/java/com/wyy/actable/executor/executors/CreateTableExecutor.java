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
public class CreateTableExecutor extends MySqlExecutor {

    public void create(String tableName, List<CodeColumn> codeColumnList) throws SQLException {
        if (codeColumnList.size() == 0) {
            return;
        }

        StringBuilder sb = new StringBuilder("create table ");
        sb.append(tableName);
        sb.append(" (\n");
        sb.append(appendColumn(codeColumnList));
        sb.append(");");
        execute(sb.toString());
    }
}

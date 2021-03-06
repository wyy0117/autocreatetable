package com.wyy.actable.executor.executors;

import com.wyy.actable.command.CodeColumn;
import com.wyy.actable.executor.MySqlExecutor;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Date: 20-2-6
 * @Author: wyy
 */
@Component
public class AddUniqueExecutor extends MySqlExecutor {
    public void add(String tableName, Set<CodeColumn> codeColumnSet) throws SQLException {
        if (codeColumnSet.size() == 0) {
            return;
        }
        Map<String, List<CodeColumn>> indexName_codeColumnList_map = codeColumnSet.stream().filter(codeColumn -> codeColumn.getUniqueName() != null && !codeColumn.getUniqueName().equals(""))
                .collect(Collectors.groupingBy(CodeColumn::getUniqueName));
        for (Map.Entry<String, List<CodeColumn>> entry : indexName_codeColumnList_map.entrySet()) {
            String indexName = entry.getKey();
            List<CodeColumn> columnList = entry.getValue();
            StringBuilder sb = new StringBuilder();
            sb.append("create unique index ");
            sb.append(indexName);
            sb.append(" on ");
            sb.append(tableName);
            sb.append("(");
            for (int i = 0; i < columnList.size(); i++) {
                CodeColumn codeColumn = columnList.get(i);
                sb.append(codeColumn.getName());
                if (i != columnList.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append(");");
            execute(sb.toString());
        }
    }
}

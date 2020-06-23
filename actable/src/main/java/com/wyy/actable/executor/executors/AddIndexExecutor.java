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
public class AddIndexExecutor extends MySqlExecutor {

    /**
     * @param tableName
     * @param codeColumnSet 会过滤不需要索引的列，
     */
    public void add(String tableName, Set<CodeColumn> codeColumnSet) throws SQLException {
        if (codeColumnSet.size() == 0) {
            return;
        }
        Map<String, List<CodeColumn>> indexName_codeColumnList_map = codeColumnSet.stream().filter(codeColumn -> codeColumn.getIndexName() != null && !codeColumn.getIndexName().equals(""))
                .collect(Collectors.groupingBy(CodeColumn::getIndexName));
        for (Map.Entry<String, List<CodeColumn>> entry : indexName_codeColumnList_map.entrySet()) {
            String indexName = entry.getKey();
            List<CodeColumn> columnList = entry.getValue();
            StringBuilder sb = new StringBuilder();
            sb.append("create index ");
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

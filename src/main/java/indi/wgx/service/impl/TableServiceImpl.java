package indi.wgx.service.impl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import indi.wgx.service.TableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  根据一句 sql 获取该 sql 有关的表名称
 */

@Service
@Slf4j
public class TableServiceImpl implements TableService{

    @Autowired
    JdbcTemplate jdbcTemplate;

    public String getTableName(String sql) {
        String dbType = JdbcConstants.MYSQL;

        // 格式化输出
//        String format = SQLUtils.format(sql, dbType);
//        log.info("SQL语句格式化" + format);

        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, dbType);
        StringBuilder stringBuilder = new StringBuilder();

        for (SQLStatement stmt : sqlStatements) {
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            // 使用深度优先算法遍历所有的语法节点
            // ,如果遇到SQLExprTableSource节点,则会调用TableVisitor的 visit() 方法
            stmt.accept(visitor);
            Set<TableStat.Name> names = visitor.getTables().keySet();
            int setSize = names.size();
            int i = 0;
            for (TableStat.Name item : names){
                if (i != setSize - 1) {
                    stringBuilder.append(item).append(";");
                } else {
                    stringBuilder.append(item);
                }
                i++;
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public String getDatabaseName() {
        String str = "select database()";
        return (String) jdbcTemplate.queryForList(str).get(0).get("database()");
    }

//    public static void main(String[] args) {
//        String sql = "select project_name, project_version, table_name, sql_content, content_score,  sql_template, template_score\n" +
//                "from project_version pv\n" +
//                "left join sql_template st\n" +
//                "on pv.project_version_id = st.project_version_id\n" +
//                "left join sql_execution se\n" +
//                "on st.sql_template_id = se.sql_template_id\n" +
//                "where pv.project_version_id = 10001\n" +
//                "order by sql_content DESC;";
//        TableServiceImpl tableService = new TableServiceImpl();
//        String tableName = tableService.getTableName(sql);
//        System.out.println(tableName);
//    }
}



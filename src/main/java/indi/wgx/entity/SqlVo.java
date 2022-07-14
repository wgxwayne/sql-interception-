package indi.wgx.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlVo {

    /**
     * 这里存放拦截器中拦截到的数据
     */

    // 具体Sql内容
    private String sqlContent;
    // Sql模板内容
    private String sqlTemplate;
    // 表的名称
    private String tableName;
    // 数据库名称
    private String databaseName;
    // 耗时
    private long time;

}

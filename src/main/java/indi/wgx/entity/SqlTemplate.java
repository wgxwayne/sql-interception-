package indi.wgx.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlTemplate {
    private Long sqlTemplateId;
    private Long projectVersionId;
    private String sqlTemplate;
    private Double templateScore;
    private String databaseName;
    private String tableName;

    private String minScoreSqlContext;
    private Double averageSqlTime;
    private Integer maxSqlTime;
    private Integer minSqlTime;

    private Timestamp gmtCreate;
    private Timestamp gmtModified;

}

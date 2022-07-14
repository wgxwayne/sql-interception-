package indi.wgx.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlExecution {
    private Long executionId;
    private Long sqlTemplateId;
    private String sqlContent;
    private Double contentScore;
    private Long sqlTime;
    private Double timeScore;

    private Timestamp gmtCreate;
    private Timestamp gmtModified;
}

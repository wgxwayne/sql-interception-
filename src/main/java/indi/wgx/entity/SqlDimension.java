package indi.wgx.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlDimension {
    private Long dimensionId;
    private Long executionId;
    private String executionField;
    private Integer rowsNum;
    private String value;
    private Double fieldScore;

    private Timestamp gmtCreate;
    private Timestamp gmtModified;

}

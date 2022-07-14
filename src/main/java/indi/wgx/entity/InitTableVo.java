package indi.wgx.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitTableVo {
    ProjectVersion projectVersion;

    SqlTemplate sqlTemplate;

    SqlExecution sqlExecution;

    List<SqlDimension> sqlDimensionList;
}

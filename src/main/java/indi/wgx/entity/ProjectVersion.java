package indi.wgx.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectVersion {
    private Long projectVersionId;
    private String projectId;
    private String projectName;
    private String projectVersion;

    private Timestamp gmtCreate;
    private Timestamp gmtModified;
}

package indi.wgx.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 获取项目的名称，版本，描述等信息
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Configuration
public class ProjectVersionVo {
    @Value("${name}")
    private String name;

    @Value("${version}")
    private String version;

    @Value("${projectId}")
    private String projectId;
}

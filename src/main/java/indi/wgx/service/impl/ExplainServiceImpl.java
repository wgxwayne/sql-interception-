package indi.wgx.service.impl;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import indi.wgx.entity.*;
import indi.wgx.service.ExplainService;
import indi.wgx.utils.SnowflakeIdWorkerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ExplainServiceImpl implements ExplainService {

    // 获取项目名称和版本信息
    @Autowired
    ProjectVersionVo projectVersionVo;

    // 执行SQL语句
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 传入sql实体,初始化所有的数据表
     * @param
     * @return
     */
    public InitTableVo getAllInitTableInformation(SqlVo sqlVo){

        // 雪花漂移算法
        IdGeneratorOptions options = new IdGeneratorOptions((short) 1);
        YitIdHelper.setIdGenerator(options);

        InitTableVo initTableVo = new InitTableVo();

        // 1、项目名称、项目版本
        ProjectVersion projectVersion = new ProjectVersion();
        projectVersion.setProjectId(projectVersionVo.getProjectId());
        projectVersion.setProjectName(projectVersionVo.getName());
        projectVersion.setProjectVersion(projectVersionVo.getVersion());
        // 雪花id算法生成项目版本id
        Long projectVersionId = YitIdHelper.nextId();
        projectVersion.setProjectVersionId(projectVersionId);
        initTableVo.setProjectVersion(projectVersion);


        // 2、sql模板信息填充
        SqlTemplate sqlTemplate = new SqlTemplate();
        // 模板id
        Long templateId = YitIdHelper.nextId();
        sqlTemplate.setSqlTemplateId(templateId);
        sqlTemplate.setProjectVersionId(projectVersionId);
        sqlTemplate.setSqlTemplate(sqlVo.getSqlTemplate());
        sqlTemplate.setDatabaseName(sqlVo.getDatabaseName());
        sqlTemplate.setTableName(sqlVo.getTableName());
        initTableVo.setSqlTemplate(sqlTemplate);


        // 3、具体sql执行信息填充
        SqlExecution sqlExecution = new SqlExecution();
        Long executionId = YitIdHelper.nextId();
        sqlExecution.setExecutionId(executionId);
        sqlExecution.setSqlTemplateId(templateId);
        sqlExecution.setSqlContent(sqlVo.getSqlContent());
        sqlExecution.setSqlTime(sqlVo.getTime());
        initTableVo.setSqlExecution(sqlExecution);


        // 4、获取explain执行结果信息
        // 将explains的结果保存到sqlDimensionList中
        List<SqlDimension> sqlDimensionList = new ArrayList<>();
        List<Map<String, Object>> explains = getExplain(sqlVo.getSqlContent());
        SqlDimension sqlDimension;
        int listSize = explains.size();
        for (int num = 0; num < listSize; num++) {
            Map<String, Object> oneExplainMap = explains.get(num);
            // 逐行遍历map
            for(Map.Entry<String, Object> entry : oneExplainMap.entrySet()) {
                sqlDimension = new SqlDimension();
                String field = entry.getKey();
                String value = String.valueOf(entry.getValue());
                // 维度id
                Long dimensionId = YitIdHelper.nextId();
                if (field.equals("Extra") || field.equals("type") || field.equals("rows")) {
                    sqlDimension.setDimensionId(dimensionId);
                    sqlDimension.setExecutionId(executionId);
                    sqlDimension.setExecutionField(field);
                    sqlDimension.setRowsNum(num + 1);  // num从0开始，但是数据库表显示第一行开始
                    sqlDimension.setValue(value);
                    sqlDimensionList.add(sqlDimension);
                }
            }
        }
        initTableVo.setSqlDimensionList(sqlDimensionList);

        return initTableVo;
    }

    /**
     * 传入一条具体的sql执行
     * 使用 jdbcTemplate
     *
     * 每一条explain语句都需要返回结果
     *
     * FORMAT=JSON: https://cloud.tencent.com/developer/article/1655411
     */
    public List<Map<String, Object>> getExplain(String sql) {
        sql = "explain " + sql;
        return jdbcTemplate.queryForList(sql);
    }

}


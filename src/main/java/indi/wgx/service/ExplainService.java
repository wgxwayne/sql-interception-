package indi.wgx.service;

import indi.wgx.entity.InitTableVo;
import indi.wgx.entity.SqlVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ExplainService {
    List<Map<String, Object>> getExplain(String sql);

    InitTableVo getAllInitTableInformation(SqlVo sqlVo);
}

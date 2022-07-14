package indi.wgx.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface TableService {
    String getTableName(String sql);

    // 得到当前正在运行的sql的数据库
    String getDatabaseName();
}

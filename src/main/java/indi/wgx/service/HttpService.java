package indi.wgx.service;

import indi.wgx.entity.InitTableVo;
import org.springframework.stereotype.Service;

@Service
public interface HttpService {
    void sendInitTableMessage(InitTableVo initTableVo);

    void sendMessageByWebClient(InitTableVo initTableVo);
}

package indi.wgx.service.impl;

import indi.wgx.entity.InitTableVo;
import indi.wgx.service.HttpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Service
@Slf4j
public class HttpServiceImpl implements HttpService {

    @Resource
    RestTemplate restTemplate;

    @Value("${url-api}")
    private String url;

    /**
     * 传输 InitTableVo 对象，并返回该对象
     *
     * 使用 RestTemplate 发送消息
     */
    @Override
    public void sendInitTableMessage(InitTableVo initTableVo) {
        // 发送消息
        restTemplate.postForEntity(url, initTableVo, InitTableVo.class);
    }

    // http://localhost:8080/init/getAllData
    @Override
    public void sendMessageByWebClient(InitTableVo initTableVo) {
//        WebClient webClient = WebClient.create("http://localhost:8080");
//        Mono<Void> voidMono = webClient.method(HttpMethod.POST).uri("/init/getAllData").syncBody(initTableVo).retrieve().bodyToMono(Void.class);
//        voidMono.block();
    }
}

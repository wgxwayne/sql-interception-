package indi.wgx.config;

import indi.wgx.interceptor.InterceptorForQry;
import indi.wgx.interceptor.MybatisInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "indi.wgx")
@ConditionalOnProperty(name = "explain.interception", havingValue = "true")
public class MybatisConfiguration {

    @Bean
    public MybatisInterceptor mybatisInterceptor() {
        return new MybatisInterceptor();
    }

    /**
     * 拦截SQL执行的结果。需要的时候再去掉注释
     */
//    @Bean
//    public InterceptorForQry interceptorForQry() {
//        return new InterceptorForQry();
//    }
}

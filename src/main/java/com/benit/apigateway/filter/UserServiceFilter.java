package com.benit.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class UserServiceFilter extends AbstractGatewayFilterFactory<UserServiceFilter.Config> {



    // 생성자
    public UserServiceFilter() {
        super(Config.class);
    }

    // Config inner class
    public static class Config{}


    // 필터 메인
    @Override
    public GatewayFilter apply(Config config) {

        return ((exchange, chain) -> {

            StopWatch stopWatch = new StopWatch("api gateway");
            // Request, Response 객체 가져오기
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            // PRE
            stopWatch.start();
            log.info("[UserServiceFilter 필터] REQUEST 요청 >>>> IP : {}, URI : {}",  request.getRemoteAddress().getAddress(), request.getURI() );


            // POST
            return chain.filter(exchange).then(Mono.fromRunnable(()->{

                stopWatch.stop();
                log.info("[UserServiceFilter 필터] RESPONSE 응답 >>>> IP : {}, URI : {}, 응답코드 : {}  --> 처리시간 : {} ms",
                        request.getRemoteAddress().getAddress(),
                        request.getURI(),
                        response.getStatusCode(),
                        stopWatch.getLastTaskTimeMillis()
                );

            }));
        });

    }
}
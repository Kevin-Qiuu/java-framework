package com.bitejiuyeke.bitegateway.handler;

import com.bitejiuyeke.bitecommoncore.utils.JsonUtil;
import com.bitejiuyeke.bitecommoncore.utils.ServletUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

// Spring Cloud Gateway 之所以必须使用 ErrorWebExceptionHandler，是因为它选择了基于 Spring WebFlux 的高性能、非阻塞响应式架构。
// 作为 WebFlux 原生的异常处理基石，ErrorWebExceptionHandler 是唯一能够与网关的整体架构和数据流处理方式无缝集成的机制。
// 网关的异常处理与 Spring mvc 的异常处理机制不同，网关并不能显示指定异常的类来进行特定的异常处理。
// 而是所有的异常都会由 ErrorWebExceptionHandler 进行处理
@Configuration
@Order(-1)
@Slf4j
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        ServerHttpResponse response = exchange.getResponse();
        // 响应已经提交给了客户端，无法再对这个响应进行常规的异常处理
        // 直接返回一个包含原始异常的 Mono.error(ex)
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        Integer retCode = ResultCode.ERROR.getCode();
        String retMsg = ResultCode.ERROR.getMsg();

        // 对于 NoResourceFoundException 进行处理
        if (ex instanceof NoResourceFoundException) {
            retCode = ResultCode.SERVICE_NOT_FOUND.getCode();
            retMsg = ResultCode.SERVICE_NOT_FOUND.getMsg();
        }

        // 设置 http 报文状态码
        HttpStatus httpStatus = HttpStatus.valueOf(Integer.parseInt(String.valueOf(retCode).substring(0, 3)));
        log.error("【网关异常处理】请求路径：{}，异常信息：{}", exchange.getRequest().getPath(), ex.getMessage());

        // 向已订阅的 WebFlux 框架中流式报文中写入信息
        return ServletUtil.webFluxResponseWriter(response, httpStatus, retCode, retMsg);
    }


}

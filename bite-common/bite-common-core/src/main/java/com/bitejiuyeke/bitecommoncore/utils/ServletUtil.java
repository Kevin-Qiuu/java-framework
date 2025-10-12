package com.bitejiuyeke.bitecommoncore.utils;

import com.bitejiuyeke.bitecommondomain.constants.CommonConstants;
import com.bitejiuyeke.bitecommondomain.domain.R;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ServletUtil {

    /**
     * 内容编码 (UTF-8)
     *
     * @param data 内容
     * @return 编码后的内容
     */
    public static String urlEncode(String data) {
        return URLEncoder.encode(data, StandardCharsets.UTF_8);
    }


    /**
     * 获取当前请求的 HttpServletRequest
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        try {
            return Objects.requireNonNull(getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前请求的 ServletRequestAttributes
     *
     * <p>Tomcat 会为每个 HTTP 连接分配一个线程，而 RequestContextHolder 实际上是基于 ThreadLocal 实现的，
     * 因此可以通过它来获取与当前线程绑定的 HttpServletRequest 和 HttpServletResponse 等对象。</p>
     *
     * <p>基于 ThreadLocal 的特性，使得在同一个请求线程中的任意位置都可以方便地获取到当前请求的相关信息，
     * 从而实现对 HTTP 报文的访问和操作。</p>
     *
     * 注意的是这个方法只适用于传统的网络模型（Spring MVC），不适用于网关的 WebFlux 架构，因为网关的线程处理是不固定的，
     * 因此 ThreadLocal 就失效了
     */
    public static ServletRequestAttributes getRequestAttributes() {

        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            return (ServletRequestAttributes) requestAttributes;
        } catch (Exception e) {
            return null;
        }
    }

    // 为了使 ContentType 与调用方解耦
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, HttpStatus httpStatus,
                                             Integer retCode, Object value) {
        return webFluxResponseWriter(response, MediaType.APPLICATION_JSON_VALUE,
                httpStatus, retCode, value);
    }


    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, String contentType, HttpStatus httpStatus,
                                             Integer retCode, Object value) {

        response.setStatusCode(httpStatus);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);

        R<Object> failResult = R.fail(retCode, String.valueOf(value));
        DataBuffer dataBuffer = response.bufferFactory()
                .wrap(JsonUtil.obj2String(failResult).getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(dataBuffer));
    }

}

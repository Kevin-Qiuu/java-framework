package com.bitejiuyeke.bitecommoncore.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

public class ServletUtil {

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
     */
    public static ServletRequestAttributes getRequestAttributes() {

        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            return (ServletRequestAttributes) requestAttributes;
        } catch (Exception e) {
            return null;
        }
    }

}

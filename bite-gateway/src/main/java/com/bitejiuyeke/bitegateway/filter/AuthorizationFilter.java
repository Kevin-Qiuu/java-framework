package com.bitejiuyeke.bitegateway.filter;

import com.bitejiuyeke.bitecommoncore.utils.ServletUtil;
import com.bitejiuyeke.bitecommoncore.utils.StringUtil;
import com.bitejiuyeke.bitecommondomain.constants.LoginUserConstants;
import com.bitejiuyeke.bitecommondomain.constants.SecurityConstants;
import com.bitejiuyeke.bitecommondomain.constants.TokenConstants;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.LoginUserDTO;
import com.bitejiuyeke.bitecommonsecurity.service.TokenService;
import com.bitejiuyeke.bitecommonsecurity.utils.JwtUtil;
import com.bitejiuyeke.bitegateway.config.IgnoredUriWhiteList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


@Component
public class AuthorizationFilter implements GlobalFilter, Ordered {

    @Autowired
    private IgnoredUriWhiteList ignoredUriWhiteList;

    @Autowired
    private TokenService tokenService;

    /**
     * 网关的身份校验过滤逻辑
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        /*
        1、获取请求
        2、判断请求是否处于白名单中
        3、获取 token
        4、校验 token
        5、获取用户数据信息
        6、将用户信息放入待转发的 http 报文中
         */

        // 1、获取请求
        ServerHttpRequest request = exchange.getRequest();

        // 2、判断请求的 uri 是否处于白名单中
        String uri = request.getURI().getPath();
        List<String> whiteList = ignoredUriWhiteList.getWhiteList();
        if (whiteList.isEmpty() || StringUtil.matches(uri, whiteList)) {
            return chain.filter(exchange);
        }

        // 3、获取 token
        String token = getTokenFromRequestHeader(request);
        if (StringUtils.isEmpty(token)) {
            return unauthorizedResponse(exchange, ResultCode.TOKEN_EMPTY);
        }

        // 4、校验 token 是否有效，防止后续 Redis 校验浪费网络资源
        if (!JwtUtil.verifyTokenValid(token)) {
            return unauthorizedResponse(exchange, ResultCode.TOKEN_INVALID);
        }

        // 5、校验用户是否处于登录态
        if (tokenService.isTokenExpired(token)) {
            return unauthorizedResponse(exchange, ResultCode.TOKEN_OVERTIME);
        }

        // 6、从 token 中获取用户信息
        LoginUserDTO loginUser = tokenService.getLoginUser(token);

        // 7、将用户信息设置到 http 转发报文头中，用于下游服务校验
        ServerHttpRequest updatedHttpRequest = request.mutate()
                .header(LoginUserConstants.USER_KEY, ServletUtil.urlEncode(loginUser.getUserKey()))
                .header(LoginUserConstants.USER_ID, ServletUtil.urlEncode(loginUser.getUserId()))
                .header(LoginUserConstants.USERNAME, ServletUtil.urlEncode(loginUser.getUsername()))
                .header(LoginUserConstants.USER_FROM, ServletUtil.urlEncode(loginUser.getUserFrom()))
                .build();

        // 8、将上述的请求报文写入 ServerWebExchange 后执行后续业务链
        ServerWebExchange updatedExchange = exchange.mutate().request(updatedHttpRequest).build();
        return chain.filter(updatedExchange);
    }

    /**
     * 处理校验失败，设置报文响应码以及内容
     *
     * @param exchange   当前的 ServerWebExchange
     * @param resultCode 结果码
     * @return Mono<Void> 用于发送执行成功信号
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, ResultCode resultCode) {
        int statusCode = Integer.parseInt(String.valueOf(resultCode.getCode()).substring(0, 3));
        return ServletUtil.webFluxResponseWriter(exchange.getResponse(), HttpStatus.valueOf(statusCode),
                resultCode.getCode(), resultCode.getMsg());
    }


    /**
     * 从 Http 请求报文头中获取 token 内容，如果 token 有前缀则将其裁剪返回
     *
     * @param request Http 请求
     * @return token 内容
     */
    private String getTokenFromRequestHeader(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(SecurityConstants.AUTHORIZATION);
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return token;
    }

    /**
     * Ordered 接口可以由用户指定 Spring 的 Component 的执行顺序
     *
     * @return 执行顺序
     */
    @Override
    public int getOrder() {
        return -99;
    }
}

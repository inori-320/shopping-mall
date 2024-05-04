package com.hmall.gateway.filters;

import com.hmall.gateway.config.AuthProperties;
import com.hmall.gateway.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author lty
 */
@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    private final AuthProperties authProperties;
    private final JwtTool jwtTool;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取request
        ServerHttpRequest request = exchange.getRequest();
        // 判断是否需要做登录校验
        if(isExclude(request.getPath().toString())){
            return chain.filter(exchange);
        }
        // 获取token
        List<String> authorization = request.getHeaders().get("authorization");
        String token = null;
        if(authorization != null && !authorization.isEmpty()){
            token = authorization.get(0);
        }
        // 校验并解析token
        Long userId;
        try {
            // 从token中解析出用户ID
            userId = jwtTool.parseToken(token);
        } catch (Exception e){
            // 拦截，设置状态码401
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        // 传递用户信息
        ServerWebExchange build = exchange.mutate()
                .request(builder -> builder.header("user-info", userId.toString())).build();
        return chain.filter(build);
    }

    private boolean isExclude(String path) {
        for (String pathPattern : authProperties.getExcludePaths()) {
            if(antPathMatcher.match(pathPattern, path)) return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

package com.hmall.api.config;

import com.hmall.common.utils.UserContext;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

/**
 * @author lty
 */
public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLogger(){
        return Logger.Level.BASIC;
    }

    @Bean
    public RequestInterceptor userInfoRequestInterceptor(){
        return requestTemplate -> {
            Long user = UserContext.getUser();
            if(user != null){
                requestTemplate.header("user-info", user.toString());
            }
        };
    }
}

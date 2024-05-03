package com.hmall.api.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * @author lty
 */
public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLogger(){
        return Logger.Level.BASIC;
    }
}

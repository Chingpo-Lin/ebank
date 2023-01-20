package org.example.config;

import feign.RequestInterceptor;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration
@Data
public class AppConfig {

    /**
     * feign lost token solution, add interceptor
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest httpServletRequest = attributes.getRequest();
                if (httpServletRequest == null) {
                    return;
                }
                String token = httpServletRequest.getHeader("token");
                requestTemplate.header("token", token);
            }
        };
    }
}

package com.moji.server;

import com.moji.server.slack.filter.LogbackMdcFilter;
import com.moji.server.slack.filter.MultiReadableHttpServletRequestFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean multiReadableHttpServletRequestFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        MultiReadableHttpServletRequestFilter multiReadableHttpServletRequestFilter = new MultiReadableHttpServletRequestFilter();
        registrationBean.setFilter(multiReadableHttpServletRequestFilter);
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean logbackMdcFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        LogbackMdcFilter logbackMdcFilter = new LogbackMdcFilter();
        registrationBean.setFilter(logbackMdcFilter);
        registrationBean.setOrder(2);
        return registrationBean;
    }
}

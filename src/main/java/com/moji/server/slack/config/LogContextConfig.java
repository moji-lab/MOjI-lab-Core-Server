package com.moji.server.slack.config;

import ch.qos.logback.classic.LoggerContext;
import com.moji.server.slack.CustomLogbackAppender;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

/**
 * Created By ds on 29/09/2019.
 */

@Configuration
public class LogContextConfig implements InitializingBean {

    private final LogConfig logConfig;

    public LogContextConfig(final LogConfig logConfig) {
        this.logConfig = logConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        CustomLogbackAppender customLogbackAppender = new CustomLogbackAppender(logConfig);

        customLogbackAppender.setContext(loggerContext);
        customLogbackAppender.setName("customLogbackAppender");
        customLogbackAppender.start();
        loggerContext.getLogger("ROOT").addAppender(customLogbackAppender);
    }
}
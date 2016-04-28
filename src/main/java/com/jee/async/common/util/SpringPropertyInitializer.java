package com.jee.async.common.util ;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.context.ConfigurableWebApplicationContext;

public class SpringPropertyInitializer implements ApplicationContextInitializer<ConfigurableWebApplicationContext> {
	private final static Logger logger = LoggerFactory.getLogger(SpringPropertyInitializer.class);
	public void initialize(ConfigurableWebApplicationContext ctx) {
		try {
			PropertySource<Map<String, Object>> ps = new ResourcePropertySource("classpath:properties/application.properties");
			ctx.getEnvironment().getPropertySources().addFirst(ps);			
		}
		catch(IOException ioe) {
			logger.error(StringUtils.EMPTY, ioe);
		}
	}
}

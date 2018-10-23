package com.example.springboot.app;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class MvcConfig extends WebMvcConfigurationSupport{
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		super.addResourceHandlers(registry);
		
		String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString(); //toUri nos añade "file:"
		log.info(resourcePath);
		
		registry.addResourceHandler("/uploads/**") //Cualquier archivo o subcarpeta que tengamos dentro
		.addResourceLocations(resourcePath);
	}

}

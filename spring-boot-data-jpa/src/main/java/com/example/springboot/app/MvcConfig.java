package com.example.springboot.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class MvcConfig extends WebMvcConfigurationSupport{

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		super.addResourceHandlers(registry);
		registry.addResourceHandler("/uploads/**") //Cualquier archivo o subcarpeta que tengamos dentro
		.addResourceLocations("file:/home/rafarrale/Documentos/img_curso/uploads/");
	}

}

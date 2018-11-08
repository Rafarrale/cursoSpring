package com.example.springboot.app;



import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class MvcConfig implements WebMvcConfigurer{
	
	//private Logger log = LoggerFactory.getLogger(getClass());
	/*
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		super.addResourceHandlers(registry);
		
		String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString(); //toUri nos añade "file:"
		log.info(resourcePath);
		
		registry.addResourceHandler("/uploads/**") //Cualquier archivo o subcarpeta que tengamos dentro
		.addResourceLocations(resourcePath);
	}*/

}

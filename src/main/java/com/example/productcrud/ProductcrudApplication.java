package com.example.productcrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ProductcrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductcrudApplication.class, args);
	}

	// ⬇ This bean maps the URL path /uploads/** to the local uploads/ folder
	@Bean
	public WebMvcConfigurer webMvcConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/uploads/**")
						.addResourceLocations("file:uploads/");
				// "file:" means it's a directory on the disk
			}
		};
	}
}

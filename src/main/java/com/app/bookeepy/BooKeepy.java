package com.app.bookeepy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
public class BooKeepy {

	public static void main(String[] args) {
		SpringApplication.run(BooKeepy.class, args);
	}
	
	
	@Bean
	public OpenAPI customOpenAPI(@Value("${application-description}") String appDescription,
			@Value("${application-version}") String appVersion) {
		
		return new OpenAPI()
				.info(new Info()
				.title("BooKeepy API")
				.contact(new Contact().name("Pete João Chiboleca").email("pete9450@gmail.com").url("https://github.com/DanikOfficial/BooKeepy"))
				.description(appDescription)
				.version(appVersion));
		
	}

}

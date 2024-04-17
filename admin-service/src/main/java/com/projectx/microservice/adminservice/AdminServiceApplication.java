package com.projectx.microservice.adminservice;

import com.projectx.microservice.utils.ErrorHandlerComponent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class AdminServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminServiceApplication.class, args);
	}

	@Bean
	public ErrorHandlerComponent handlerComponent() {
		return new ErrorHandlerComponent();
	}
}

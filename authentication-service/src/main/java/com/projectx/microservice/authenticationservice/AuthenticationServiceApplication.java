package com.projectx.microservice.authenticationservice;

import com.projectx.microservice.authenticationservice.services.UserService;
import com.projectx.microservice.utils.Constants;
import com.projectx.microservice.utils.ErrorHandlerComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthenticationServiceApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (!userService.isUserExist(Constants.MOBILE)) {
			userService.addUser(userService.buildUser(),true);
		}
	}

	@Bean
	public ErrorHandlerComponent handlerComponent() {
		return new ErrorHandlerComponent();
	}
}

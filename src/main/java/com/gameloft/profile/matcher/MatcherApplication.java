package com.gameloft.profile.matcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.gameloft.profile.matcher.rest")
@ComponentScan(basePackages = "com.gameloft.profile.matcher.converter")
public class MatcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatcherApplication.class, args);
	}

}

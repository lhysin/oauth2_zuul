package io.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"io.test"})
@EnableZuulProxy
@SpringBootApplication
@EnableAutoConfiguration
public class Oauth2GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(Oauth2GatewayApplication.class, args);
	}

}

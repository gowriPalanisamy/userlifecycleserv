package com.tigeranalystics.userlifecycleserv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.tigeranalystics.userlifecycleserv")
public class UserlifecycleservApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserlifecycleservApplication.class,args);
	}

}

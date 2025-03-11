package com.learnspringbatch.springbatchdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication (scanBasePackages = { "com.learnspringbatch.springbatchdemo.controller", "com.learnspringbatch.springbatchdemo.config" })
public class SpringbatchdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbatchdemoApplication.class, args);
		System.out.println("This is a Test");
	}
}

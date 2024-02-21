package com.example.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

//@SpringBootApplication
//public class AttendanceApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(AttendanceApplication.class, args);
//	}
//
//}
@EnableAsync
@SpringBootApplication
public class AttendanceApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AttendanceApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(AttendanceApplication.class, args);
	}
}
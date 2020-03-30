package com.irabbit.rmq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.irabbit.rmq.mapper")
public class RmqProApplication {

	public static void main(String[] args) {
		SpringApplication.run(RmqProApplication.class, args);
	}

}

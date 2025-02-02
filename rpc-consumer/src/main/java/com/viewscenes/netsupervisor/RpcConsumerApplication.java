package com.viewscenes.netsupervisor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RpcConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RpcConsumerApplication.class, args);
	}
}

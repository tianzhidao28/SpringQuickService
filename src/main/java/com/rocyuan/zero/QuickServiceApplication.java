package com.rocyuan.zero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
@ComponentScan
public class QuickServiceApplication {

	public static void main(String[] args) throws Exception {
		// java -jar target/demo-0.0.1-SNAPSHOT.jar --server.port=8080
		SpringApplication.run(QuickServiceApplication.class, args);
	}
}

package com.rocyuan.zero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
@ComponentScan
public class QuickServiceApplication {

	public static void main(String[] args) throws Exception {
		// java -jar target/demo-0.0.1-SNAPSHOT.jar --server.port=8080
		ApplicationContext applicationContext = SpringApplication.run(QuickServiceApplication.class, args);


		System.out.println("Let's inspect the beans provided by Spring Boot:");
		String beanNames[] = applicationContext.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames)	{
			System.out.println("beanName : " + beanName);
		}


	}
}

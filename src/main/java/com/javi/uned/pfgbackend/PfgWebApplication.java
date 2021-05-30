package com.javi.uned.pfgbackend;

import com.javi.uned.pfgbackend.config.DatabaseLoggerAppender;
import com.javi.uned.pfgbackend.repositories.LogRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PfgWebApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(PfgWebApplication.class, args);
		DatabaseLoggerAppender.setRecordRepository(context.getBean(LogRepository.class));
	}

}

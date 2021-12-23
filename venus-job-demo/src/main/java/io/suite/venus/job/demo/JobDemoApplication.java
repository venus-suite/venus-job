package io.suite.venus.job.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})

 public class JobDemoApplication {


	public static void main(String[] args) {

        SpringApplication.run(JobDemoApplication.class, args);
	}

}
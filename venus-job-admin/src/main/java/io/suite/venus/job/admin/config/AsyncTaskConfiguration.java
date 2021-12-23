package io.suite.venus.job.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@EnableAsync
@Configuration
public class AsyncTaskConfiguration {
    
	@Bean
    public AsyncTaskExecutor taskExecutor() {  
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor(); 
        executor.setThreadNamePrefix("Default-Async-Executor-");
        executor.setMaxPoolSize(50);
        executor.setCorePoolSize(10);
        executor.setQueueCapacity(2000);
        // 设置拒绝策略
        return executor;  
    } 
    
    
 
}
package com.blockchain.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.TimeZone;

/**
 *
 * 鍖哄潡閾惧钩鍙板簲鐢ㄥ惎鍔ㄧ被
 * @author denglong
 * @create 2019骞�07鏈�15鏃�14:04:45
 * @version 1.0.1
 *
 */
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class PlatformApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		SpringApplication.run(PlatformApplication.class, args);
	}

	@Bean
	public TaskScheduler taskScheduler(){
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(20);
		taskScheduler.initialize();
		return taskScheduler;
	}
}

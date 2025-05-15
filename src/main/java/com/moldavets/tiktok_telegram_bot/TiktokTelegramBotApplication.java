package com.moldavets.tiktok_telegram_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TiktokTelegramBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiktokTelegramBotApplication.class, args);
	}

}

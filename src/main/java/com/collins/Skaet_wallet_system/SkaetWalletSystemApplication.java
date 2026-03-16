package com.collins.Skaet_wallet_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "AuditAwareImp")
public class SkaetWalletSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkaetWalletSystemApplication.class, args);
	}

}

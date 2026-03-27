package com.collins.Skaet_wallet_system;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "AuditAwareImp")
@OpenAPIDefinition(
		info = @Info(
				title = "Skaet Wallet System API",
				description = "A robust and scalable digital wallet service that enables users to " +
						"perform secure financial transactions including wallet funding, transfers, " +
						"withdrawals, and transaction history tracking. The system is designed with a focus " +
						"on reliability, auditability, " +
						"and high-performance transaction processing suitable for fintech environments.\"",
				version = "1.0.0",
				contact = @Contact(
						name = "Okafor Collins Daberechukwu",
						email = "collinsdaberechi20@gmail.com"
				)
		),

		servers =  {
				@Server(
						description = "Local Environment",
						url = "https://localhost:8990"
				),
				@Server(
						description = "Docker Container Network",
						url = "https://localhost:"
				)
		}
)
public class SkaetWalletSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkaetWalletSystemApplication.class, args);
	}

}

package com.collins.Skaet_wallet_system.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
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
public class SwaggerConfig {
}

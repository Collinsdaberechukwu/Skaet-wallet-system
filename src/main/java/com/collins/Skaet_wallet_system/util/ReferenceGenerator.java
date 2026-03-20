package com.collins.Skaet_wallet_system.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ReferenceGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final String ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


    public String generate() {
        // Example: TX-20260317-7F4G2A
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomPart = randomAlphaNumeric(6);
        return "TX-" + datePart + "-" + randomPart;
    }


    public String generateWalletNumber() {
        // Example: WAL-7F4G2A9B
        return "WAL-" + randomAlphaNumeric(8);
    }

    private String randomAlphaNumeric(int length){
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++){
            sb.append(ALPHANUM.charAt(random.nextInt(ALPHANUM.length())));
        }
        return sb.toString();
    }
}

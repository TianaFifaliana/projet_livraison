package com.example.livraison.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class ReferenceGenerator {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public String generateCommandeReference() {
        return "CMD-" + LocalDateTime.now().format(DATE_FORMAT) + "-"
                + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    public String generateTransactionId() {
        return "TXN-" + System.currentTimeMillis() + "-"
                + ThreadLocalRandom.current().nextInt(100000, 999999);
    }
}

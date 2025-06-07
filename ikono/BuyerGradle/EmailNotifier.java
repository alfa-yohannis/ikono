package org.example.pattern.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailNotifier implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(EmailNotifier.class);
    private final String emailAddress;

    public EmailNotifier(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public void update(String message) {
        // Simulasi pengiriman email
        logger.info("Mengirim email ke {}: {}", emailAddress, message);
        // System.out.println("Email to " + emailAddress + ": " + message);
    }
}
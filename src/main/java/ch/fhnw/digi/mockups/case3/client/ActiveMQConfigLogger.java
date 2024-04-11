package ch.fhnw.digi.mockups.case3.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ActiveMQConfigLogger {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String username;

    @Value("${spring.activemq.password}")
    private String password;

    @PostConstruct
    public void logActiveMQConfig() {
        System.out.println("ActiveMQ Broker URL: " + brokerUrl);
        System.out.println("ActiveMQ Username: " + username);
        // Avoid logging password for security reasons
    }
}


package com.sflpro.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MainSenderConfig {

    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.username}")
    private String userName;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String smtpAuth;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String starttlsEnable;
    @Value("${spring.mail.properties.debug}")
    private String debug;
    @Value("${spring.mail.protocol}")
    private String protocol;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(userName);
        javaMailSender.setPassword(password);

        Properties props = javaMailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", starttlsEnable);
        props.put("mail.smtp.ssl.trust", host);
        props.put("mail.debug", debug);
        return javaMailSender;
    }
}

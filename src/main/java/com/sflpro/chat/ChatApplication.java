package com.sflpro.chat;

import com.sflpro.chat.enums.Role;
import com.sflpro.chat.model.User;
import com.sflpro.chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaRepositories
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

        @Bean
    @Autowired
    public CommandLineRunner demoData(UserRepository userRepository) {
        return args -> {
            userRepository.save(new User("admin@gmail.com",new BCryptPasswordEncoder().encode("admin"),"Admin","Admin", Role.ADMIN));
        };
    }
}

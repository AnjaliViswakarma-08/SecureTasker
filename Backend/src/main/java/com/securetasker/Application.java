package com.securetasker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.securetasker.entity.Role;
import com.securetasker.entity.User;
import com.securetasker.repository.UserRepository;

@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public CommandLineRunner seedAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    return args -> {
      String adminEmail = "securetasker@admin.com";
      if (!userRepository.existsByEmail(adminEmail)) {
        User admin = new User();
        admin.setName("SecureTasker Admin");
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode("Admin@12345"));
        admin.setRole(Role.ROLE_ADMIN);
        userRepository.save(admin);
      }
    };
  }
}

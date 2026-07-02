package com.app.food.bootstrap;



import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.app.food.model.AppUser;
import com.app.food.model.Role;
import com.app.food.repository.AppUserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository userRepo;
    private final BCryptPasswordEncoder encoder;

    public DataInitializer(AppUserRepository userRepo, BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        String adminEmail = "admin@gmail.com";
        String adminPass = "admin123";

        userRepo.findByEmail(adminEmail).orElseGet(() -> {
            AppUser admin = new AppUser();
            admin.setFullName("Admin");
            admin.setEmail(adminEmail);
            admin.setPhone("9999999999");
            admin.setRole(Role.ADMIN);
            admin.setPasswordHash(encoder.encode(adminPass));
            return userRepo.save(admin);
        });
    }
}
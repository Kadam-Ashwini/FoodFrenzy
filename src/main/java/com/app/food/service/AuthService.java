package com.app.food.service;


import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.app.food.model.AppUser;
import com.app.food.model.Role;
import com.app.food.repository.AppUserRepository;

@Service
public class AuthService {

    private final AppUserRepository userRepo;
    private final BCryptPasswordEncoder encoder;

    public AuthService(AppUserRepository userRepo, BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    public AppUser registerUser(String fullName, String email, String phone, String rawPassword) {
        if (userRepo.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        AppUser u = new AppUser();
        u.setFullName(fullName);
        u.setEmail(email.toLowerCase().trim());
        u.setPhone(phone.trim());
        u.setPasswordHash(encoder.encode(rawPassword));
        u.setRole(Role.USER);

        return userRepo.save(u);
    }

    public Optional<AppUser> authenticate(String email, String rawPassword) {
        Optional<AppUser> opt = userRepo.findByEmail(email.toLowerCase().trim());
        if (opt.isEmpty()) return Optional.empty();

        AppUser u = opt.get();
        if (!u.isEnabled()) return Optional.empty();

        boolean ok = encoder.matches(rawPassword, u.getPasswordHash());
        return ok ? Optional.of(u) : Optional.empty();
    }
}
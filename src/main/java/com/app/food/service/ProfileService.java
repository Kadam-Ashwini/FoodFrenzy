package com.app.food.service;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.food.model.AppUser;
import com.app.food.model.UserAddress;
import com.app.food.repository.AppUserRepository;
import com.app.food.repository.UserAddressRepository;

@Service
public class ProfileService {

    private final AppUserRepository userRepo;
    private final UserAddressRepository addressRepo;

    public ProfileService(AppUserRepository userRepo, UserAddressRepository addressRepo) {
        this.userRepo = userRepo;
        this.addressRepo = addressRepo;
    }

    public AppUser getUser(Long userId) {
        return userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public void updateProfile(Long userId, String fullName, String phone) {
        AppUser u = getUser(userId);
        u.setFullName(fullName.trim());
        u.setPhone(phone.trim());
        userRepo.save(u);
    }

    public List<UserAddress> addresses(Long userId) {
        return addressRepo.findByUserIdOrderByIdDesc(userId);
    }

    public UserAddress addressForEdit(Long userId, Long addressId) {
        return addressRepo.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
    }

    @Transactional
    public void saveAddress(Long userId, UserAddress addr) {
        AppUser u = getUser(userId);
        addr.setUser(u);

        // if default checked -> unset other defaults
        if (addr.isDefault()) {
            List<UserAddress> list = addressRepo.findByUserIdOrderByIdDesc(userId);
            for (UserAddress a : list) {
                if (addr.getId() != null && a.getId().equals(addr.getId())) continue;
                if (a.isDefault()) {
                    a.setDefault(false);
                    addressRepo.save(a);
                }
            }
        }

        addressRepo.save(addr);
    }

    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        UserAddress addr = addressForEdit(userId, addressId);
        addressRepo.delete(addr);
    }

    @Transactional
    public void setDefault(Long userId, Long addressId) {
        List<UserAddress> list = addressRepo.findByUserIdOrderByIdDesc(userId);
        for (UserAddress a : list) {
            boolean makeDefault = a.getId().equals(addressId);
            if (a.isDefault() != makeDefault) {
                a.setDefault(makeDefault);
                addressRepo.save(a);
            }
        }
    }
}
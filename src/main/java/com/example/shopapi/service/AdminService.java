package com.example.shopapi.service;

import com.example.shopapi.domain.Admin;
import com.example.shopapi.domain.Member;
import com.example.shopapi.domain.Role;
import com.example.shopapi.repository.AdminRepository;
import com.example.shopapi.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(AdminRepository adminRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public Admin addAdmin(Admin admin) {
        Optional<Role> userRole = roleRepository.findByName("ROLE_ADMIN");
        admin.addRole(userRole.get());
        Admin saveAdmin= adminRepository.save(admin);
        return saveAdmin;
    }
    public Admin login(String email, String password) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            return admin; // Login successful
        }
        return null; // Login failed
    }

    public Admin signup(Admin admin) {
        // Encrypt the password
        String encodedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);

        // Save Admin object to the database
        return adminRepository.save(admin);
    }

    public Optional<Admin> getAdmin(Long adminId) {
        return adminRepository.findById(adminId);
    }

    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
}

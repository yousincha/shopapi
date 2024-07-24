package com.example.shopapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="admin")
@NoArgsConstructor
@Setter
@Getter
public class Admin {

    @Id
    @Column(name = "admin_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    @Column(length = 255, unique = true)
    private String email;

    @JsonIgnore
    @Column(length = 500)
    private String password;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_creation_time")
    private LocalDateTime resetTokenCreationTime;

    @ManyToMany
    @JoinTable(name = "admin_role",
            joinColumns = @JoinColumn(name = "admin_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", resetToken='" + resetToken + '\'' +
                '}';
    }
    // 역할 추가 메서드
    public void addRole(Role role) {
        this.roles.add(role);
    }
}

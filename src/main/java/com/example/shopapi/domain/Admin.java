package com.example.shopapi.domain;


import jakarta.persistence.*;

@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // 기본 생성자
    public Admin() {
    }

    // 이메일과 비밀번호를 인자로 받는 생성자
    public Admin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter 및 Setter 메소드
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

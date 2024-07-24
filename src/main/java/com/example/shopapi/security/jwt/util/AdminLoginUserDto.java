package com.example.shopapi.security.jwt.util;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AdminLoginUserDto {
    private Long Id;
    private String email;
    private List<String> roles = new ArrayList<> ();

    public void addRole(String role){
        roles.add(role);
    }
}

package com.example.shopapi.controller;

import com.example.shopapi.domain.Address;
import com.example.shopapi.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/saveAddress")
public class AddressController {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressController(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @PostMapping
    public ResponseEntity<String> saveAddress(@RequestBody Address address) {
        try {
            addressRepository.save(address);
            return ResponseEntity.ok("주소가 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주소 저장 중 오류가 발생했습니다.");
        }
    }
}

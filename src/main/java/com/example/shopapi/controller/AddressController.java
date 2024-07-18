package com.example.shopapi.controller;

import com.example.shopapi.domain.Address;
import com.example.shopapi.repository.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {
    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

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

    @GetMapping
    public ResponseEntity<List<Address>> getAddressList() {
        try {
            List<Address> addresses = addressRepository.findAll();
            logger.info("Fetched addresses: {}", addresses); // 로그 추가
            return ResponseEntity.ok(addresses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateAddress(@PathVariable Long id, @RequestBody Address updatedAddress) {
        try {
            Address existingAddress = addressRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("주소가 존재하지 않습니다: " + id));

            existingAddress.setRecipientName(updatedAddress.getRecipientName());
            existingAddress.setRecipientPhone(updatedAddress.getRecipientPhone());
            existingAddress.setPostalCode(updatedAddress.getPostalCode());
            existingAddress.setAddress(updatedAddress.getAddress());

            addressRepository.save(existingAddress);

            return ResponseEntity.ok("주소가 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("주소 업데이트 중 오류가 발생했습니다.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        try {
            addressRepository.deleteById(id);
            return ResponseEntity.ok("주소가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주소 삭제 중 오류가 발생했습니다.");
        }
    }
}

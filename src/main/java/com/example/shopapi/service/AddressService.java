package com.example.shopapi.service;

import com.example.shopapi.domain.Address;
import com.example.shopapi.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public List<Address> getAddressesByMemberId(Long memberId) {
        return addressRepository.findByMemberId(memberId);
    }

    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    public Optional<Address> getAddressById(Long id) {
        return addressRepository.findById(id);
    }

    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }
    public Address updateAddress(Long id, Address updatedAddress) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent()) {
            Address existingAddress = optionalAddress.get();
            existingAddress.setRecipientName(updatedAddress.getRecipientName());
            existingAddress.setRecipientPhone(updatedAddress.getRecipientPhone());
            existingAddress.setPostalCode(updatedAddress.getPostalCode());
            existingAddress.setAddress(updatedAddress.getAddress());
            return addressRepository.save(existingAddress);
        } else {
            throw new IllegalArgumentException("주소가 존재하지 않습니다: " + id);
        }
    }
}

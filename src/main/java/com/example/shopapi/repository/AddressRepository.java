// AddressRepository.java

package com.example.shopapi.repository;

import com.example.shopapi.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByMemberId(Long memberId);
}

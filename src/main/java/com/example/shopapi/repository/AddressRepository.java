// AddressRepository.java

package com.example.shopapi.repository;

import com.example.shopapi.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}

// AddressRepository.java

package com.example.shopapi.repository;

import com.example.shopapi.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
    // 추가적인 메서드가 필요하다면 여기에 작성 가능
}

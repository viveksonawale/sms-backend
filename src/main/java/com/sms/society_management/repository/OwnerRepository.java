package com.sms.society_management.repository;

import com.sms.society_management.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByFlatNumber(String flatNumber);
}

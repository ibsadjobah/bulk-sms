package com.ibsadjobah.bulksms.bulksms.repository;

import com.ibsadjobah.bulksms.bulksms.model.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{


    Optional<Customer> findByPhone(String phone);

    Optional<Customer> findByEmail(String email);
}

package com.shoppingcart.repository;

import com.shoppingcart.model.Address;
import com.shoppingcart.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository
        extends JpaRepository<Address, Long> {

    List<Address> findByUser(User user);

    Optional<Address> findByAddressIdAndUser(
            Long addressId,
            User user
    );

    Optional<Address> findByUserAndIsDefaultTrue(
            User user
    );
}
package com.shoppingcart.service;

import com.shoppingcart.dto.AddressRequest;
import com.shoppingcart.dto.AddressResponse;
import com.shoppingcart.exception.ResourceNotFoundException;
import com.shoppingcart.model.Address;
import com.shoppingcart.model.User;
import com.shoppingcart.repository.AddressRepository;
import com.shoppingcart.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;


    public AddressService(
            AddressRepository addressRepository,
            UserRepository userRepository) {

        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }


    // ==========================================
    // ADD ADDRESS
    // ==========================================

    public AddressResponse addAddress(
            Long userId,
            AddressRequest request) {

        validateUserAccess(userId);

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + userId
                        )
                );


        Address address = new Address();

        address.setUser(user);
        address.setFullName(request.getFullName());
        address.setPhone(request.getPhone());
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());
        address.setAddressType(request.getAddressType());


        boolean makeDefault =
                Boolean.TRUE.equals(request.getIsDefault());

        if (makeDefault) {
            removeExistingDefaultAddress(user);
        }

        address.setIsDefault(makeDefault);


        Address savedAddress =
                addressRepository.save(address);

        return convertToResponse(savedAddress);
    }


    // ==========================================
    // GET USER ADDRESSES
    // ==========================================

    @Transactional(readOnly = true)
    public List<AddressResponse> getUserAddresses(
            Long userId) {

        validateUserAccess(userId);

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + userId
                        )
                );


        return addressRepository
                .findByUser(user)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }


    // ==========================================
    // GET ADDRESS BY ID
    // ==========================================

    @Transactional(readOnly = true)
    public AddressResponse getAddressById(
            Long userId,
            Long addressId) {

        validateUserAccess(userId);

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + userId
                        )
                );


        Address address =
                addressRepository
                        .findByAddressIdAndUser(
                                addressId,
                                user
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Address not found with ID: "
                                                + addressId
                                )
                        );


        return convertToResponse(address);
    }


    // ==========================================
    // UPDATE ADDRESS
    // ==========================================

    public AddressResponse updateAddress(
            Long userId,
            Long addressId,
            AddressRequest request) {

        validateUserAccess(userId);

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + userId
                        )
                );


        Address address =
                addressRepository
                        .findByAddressIdAndUser(
                                addressId,
                                user
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Address not found with ID: "
                                                + addressId
                                )
                        );


        address.setFullName(request.getFullName());
        address.setPhone(request.getPhone());
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());
        address.setAddressType(request.getAddressType());


        boolean makeDefault =
                Boolean.TRUE.equals(request.getIsDefault());

        if (makeDefault) {
            removeExistingDefaultAddress(user);
        }

        address.setIsDefault(makeDefault);


        Address updatedAddress =
                addressRepository.save(address);

        return convertToResponse(updatedAddress);
    }


    // ==========================================
    // DELETE ADDRESS
    // ==========================================

    public void deleteAddress(
            Long userId,
            Long addressId) {

        validateUserAccess(userId);

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + userId
                        )
                );


        Address address =
                addressRepository
                        .findByAddressIdAndUser(
                                addressId,
                                user
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Address not found with ID: "
                                                + addressId
                                )
                        );


        addressRepository.delete(address);
    }


    // ==========================================
    // VALIDATE USER ACCESS
    // ==========================================

    private void validateUserAccess(Long userId) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ROLE_ADMIN")
                        );

        if (isAdmin) {
            return;
        }

        String loggedInEmail =
                authentication.getName();

        User loggedInUser =
                userRepository.findByEmail(loggedInEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Logged-in user not found"
                                )
                        );

        if (!loggedInUser.getUserId()
                .equals(userId)) {

            throw new RuntimeException(
                    "Access Denied: You are not authorized to access this user's address"
            );
        }
    }


    // ==========================================
    // REMOVE OLD DEFAULT ADDRESS
    // ==========================================

    private void removeExistingDefaultAddress(
            User user) {

        addressRepository
                .findByUserAndIsDefaultTrue(user)
                .ifPresent(address -> {

                    address.setIsDefault(false);

                    addressRepository.save(address);
                });
    }


    // ==========================================
    // ENTITY → DTO
    // ==========================================

    private AddressResponse convertToResponse(
            Address address) {

        return new AddressResponse(
                address.getAddressId(),
                address.getUser().getUserId(),
                address.getFullName(),
                address.getPhone(),
                address.getAddressLine(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getCountry(),
                address.getAddressType(),
                address.getIsDefault()
        );
    }
}
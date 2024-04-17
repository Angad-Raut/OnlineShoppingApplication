package com.projectx.microservice.authenticationservice.services;

import com.projectx.microservice.authenticationservice.entity.TokenDetails;
import com.projectx.microservice.authenticationservice.entity.UserAddress;
import com.projectx.microservice.authenticationservice.entity.Users;
import com.projectx.microservice.authenticationservice.payloads.*;
import com.projectx.microservice.authenticationservice.repository.UserRepository;
import com.projectx.microservice.exceptions.AlreadyExistException;
import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.payloads.EntityIdDto;
import com.projectx.microservice.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Override
    public Users getUserByUserName(String username) {
        Users users = null;
        if (Constants.isMobile(username)) {
            users = userRepository.findByUserMobile(Long.parseLong(username));
        } else {
            users = userRepository.findByUserEmail(username);
        }
        return users;
    }

    @Override
    public Boolean addUser(UserDto dto,Boolean isAdmin) throws AlreadyExistException, ResourceNotFoundException {
        Users users = null;
        if (dto.getUserId()==null) {
                isMobileExist(dto.getUserMobile());
                isEmailExist(dto.getUserEmail());
                users = Users.builder()
                        .userName(dto.getUserName())
                        .userMobile(dto.getUserMobile())
                        .userEmail(dto.getUserEmail())
                        .userRole(isAdmin?Constants.ADMIN:Constants.USER)
                        .userStatus(true)
                        .gender(dto.getGender())
                        .userAddress(setAddress(dto.getAddressDto()))
                        .isAdmin(isAdmin)
                        .insertedTime(new Date())
                        .updatedTime(new Date())
                        .userPassword(passwordEncoder.encode(dto.getUserPassword()))
                        .build();
        } else {
                users = userRepository.getUserById(dto.getUserId());
                if (users!=null) {
                    if (!dto.getUserName().equals(users.getUserName())) {
                        users.setUserName(dto.getUserName());
                    }
                    if (!dto.getGender().equals(users.getGender())) {
                        users.setGender(dto.getGender());
                    }
                    if (!dto.getUserMobile().equals(users.getUserMobile())) {
                        isMobileExist(dto.getUserMobile());
                        users.setUserMobile(dto.getUserMobile());
                    }
                    if (!dto.getUserEmail().equals(users.getUserEmail())) {
                        users.setUserEmail(dto.getUserEmail());
                    }
                    if (dto.getAddressDto()!=null && dto.getAddressDto().getBillingAddress()!=null) {
                        BillingAddress billingAddress = dto.getAddressDto().getBillingAddress();
                        if (!billingAddress.getStreet().equals(users.getUserAddress().getBillingStreet())) {
                            users.getUserAddress().setBillingStreet(billingAddress.getStreet());
                        }
                        if (!billingAddress.getCity().equals(users.getUserAddress().getBillingCity())) {
                            users.getUserAddress().setBillingCity(billingAddress.getCity());
                        }
                        if (!billingAddress.getState().equals(users.getUserAddress().getBillingState())) {
                            users.getUserAddress().setBillingState(billingAddress.getState());
                        }
                        if (!billingAddress.getPinCode().equals(users.getUserAddress().getBillingPinCode())) {
                            users.getUserAddress().setBillingPinCode(billingAddress.getPinCode());
                        }
                    }
                    if (dto.getAddressDto()!=null && dto.getAddressDto().getShippingAddress()!=null) {
                        ShippingAddress shippingAddress = dto.getAddressDto().getShippingAddress();
                        if (!shippingAddress.getStreet().equals(users.getUserAddress().getShippingStreet())) {
                            users.getUserAddress().setShippingStreet(shippingAddress.getStreet());
                        }
                        if (!shippingAddress.getCity().equals(users.getUserAddress().getShippingCity())) {
                            users.getUserAddress().setShippingCity(shippingAddress.getCity());
                        }
                        if (!shippingAddress.getState().equals(users.getUserAddress().getShippingState())) {
                            users.getUserAddress().setShippingState(shippingAddress.getState());
                        }
                        if (!shippingAddress.getPinCode().equals(users.getUserAddress().getShippingPinCode())) {
                            users.getUserAddress().setShippingPinCode(shippingAddress.getPinCode());
                        }
                    }
                    users.setUpdatedTime(new Date());
                } else {
                    throw new ResourceNotFoundException(Constants.USER_DETAILS_NOT_EXIST);
                }
        }
        try {
            return userRepository.save(users)!=null?true:false;
        } catch (AlreadyExistException e) {
            throw new AlreadyExistException(e.getMessage());
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public UserDto getById(EntityIdDto dto) throws ResourceNotFoundException {
        try {
             Users users = userRepository.getUserById(dto.getEntityId());
             if (users!=null) {
                 return UserDto.builder()
                         .userId(users.getUserId())
                         .userName(users.getUserName())
                         .gender(users.getGender())
                         .userMobile(users.getUserMobile())
                         .userEmail(users.getUserEmail())
                         .addressDto(toAddress(users.getUserAddress()))
                         .build();
             } else {
                 throw new ResourceNotFoundException(Constants.USER_DETAILS_NOT_EXIST);
             }
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<ViewUserDto> getAllUsers() {
        List<Users> fetchList = userRepository.findAll();
        AtomicInteger index = new AtomicInteger(0);
        return !fetchList.isEmpty()?fetchList.stream()
                .map(data -> ViewUserDto.builder()
                        .srNo(index.incrementAndGet())
                        .userId(data.getUserId())
                        .userName(data.getUserName())
                        .userMobile(data.getUserMobile())
                        .userEmail(data.getUserEmail())
                        .gender(data.getGender())
                        .userStatus(data.getUserStatus()?"Enable":"Disable")
                        .build())
                .collect(Collectors.toList()):new ArrayList<>();
    }

    @Override
    public Boolean isUserExist(Long mobile) {
        if (userRepository.existsByUserMobile(mobile)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public UserDto buildUser() {
        return UserDto.builder()
                .userName(Constants.NAME)
                .gender(Constants.GENDER)
                .userMobile(Constants.MOBILE)
                .userEmail(Constants.EMAIL)
                .userPassword(Constants.PASSWORD)
                .addressDto(toBuildAddress())
                .build();
    }

    @Override
    public LoginResponse saveRefreshToken(String username) throws ParseException {
        /*TokenDetails tokenDetails = tokenService.saveRefreshToken(username);
        Users users = userRepository.getUserById(tokenDetails.getUserId());
        return AuthResponse.builder()
                .userRole(users.getUserRole())
                .userName(users.getUserName())
                .accessToken(tokenDetails.getAccessToken())
                .refreshToken(tokenDetails.getRefreshToken())
                .refreshTokenId(tokenDetails.getTokenId())
                .build();*/
        return tokenService.getTokenDetails(username);
    }

    @Override
    public AuthResponse getRefreshToken(String username) throws ParseException {
        return tokenService.getRefreshToken(username);
    }

    @Override
    public CustomerInfoDto getUserInfoByMobile(EntityIdDto dto) {
        try {
            Users users = userRepository.findByUserMobile(dto.getEntityId());
            UserAddress billingAddress = users.getUserAddress();
            return users != null ? CustomerInfoDto.builder()
                    .userId(users.getUserId())
                    .userName(users.getUserName())
                    .userEmail(users.getUserEmail())
                    .userMobile(users.getUserMobile())
                    .gender(users.getGender())
                    .street(billingAddress.getBillingStreet())
                    .city(billingAddress.getBillingCity())
                    .state(billingAddress.getBillingState())
                    .pinCode(billingAddress.getBillingPinCode())
                    .build() : new CustomerInfoDto();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void isMobileExist(Long mobile) {
        if (userRepository.existsByUserMobile(mobile)) {
            throw new AlreadyExistException(Constants.MOBILE_NUMBER_ALREADY_EXIST);
        }
    }

    private void isEmailExist(String email) {
        if (userRepository.existsByUserEmail(email)) {
            throw new AlreadyExistException(Constants.EMAIL_ID_ALREADY_EXIST);
        }
    }

    private UserAddress setAddress(AddressDto addressDto) {
        BillingAddress billingAddress = addressDto.getBillingAddress();
        ShippingAddress shippingAddress = addressDto.getShippingAddress();
        return UserAddress.builder()
                .billingStreet(billingAddress.getStreet())
                .billingCity(billingAddress.getCity())
                .billingState(billingAddress.getState())
                .billingPinCode(billingAddress.getPinCode())
                .shippingStreet(shippingAddress.getStreet())
                .shippingCity(shippingAddress.getCity())
                .shippingState(shippingAddress.getState())
                .shippingPinCode(shippingAddress.getPinCode())
                .build();
    }

    private AddressDto toAddress(UserAddress address) {
        return AddressDto.builder()
                .addressId(address.getId())
                .billingAddress(BillingAddress.builder()
                        .street(address.getBillingStreet())
                        .city(address.getBillingCity())
                        .state(address.getBillingState())
                        .pinCode(address.getBillingPinCode())
                        .build())
                .shippingAddress(ShippingAddress.builder()
                        .street(address.getShippingStreet())
                        .city(address.getShippingCity())
                        .state(address.getShippingState())
                        .pinCode(address.getShippingPinCode())
                        .build())
                .build();
    }
    private AddressDto toBuildAddress() {
        return AddressDto.builder()
                .billingAddress(BillingAddress.builder()
                        .street("AT Post Fakrabad")
                        .city("Ahmednagar")
                        .state("Maharashtra")
                        .pinCode(413205)
                        .build())
                .shippingAddress(ShippingAddress.builder()
                        .street("AT Post Fakrabad")
                        .city("Ahmednagar")
                        .state("Maharashtra")
                        .pinCode(413205)
                        .build())
                .build();
    }
}

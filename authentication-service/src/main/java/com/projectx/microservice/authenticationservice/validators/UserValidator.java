package com.projectx.microservice.authenticationservice.validators;

import com.projectx.microservice.authenticationservice.payloads.BillingAddress;
import com.projectx.microservice.authenticationservice.payloads.ShippingAddress;
import com.projectx.microservice.authenticationservice.payloads.UserDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class UserValidator implements ConstraintValidator<UserValid, UserDto> {

    Boolean isValid=true;

    List<String> gendersList = Arrays.asList("male","female","other");

    @Override
    public void initialize(UserValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserDto userDto, ConstraintValidatorContext context) {
        if (userDto.getUserName()==null) {
            context.buildConstraintViolationWithTemplate("{name.not.null}")
                    .addPropertyNode("userName")
                    .addConstraintViolation();
            isValid = false;
        } else if (userDto.getUserName().length()>30) {
            context.buildConstraintViolationWithTemplate("{name.size.exceed}")
                    .addPropertyNode("userName")
                    .addConstraintViolation();
            isValid = false;
        }
        if (userDto.getUserEmail()==null) {
            context.buildConstraintViolationWithTemplate("{email.not.null}")
                    .addPropertyNode("userEmail")
                    .addConstraintViolation();
            isValid = false;
        }
        if (userDto.getUserMobile()==null) {
            context.buildConstraintViolationWithTemplate("{mobile.not.null}")
                    .addPropertyNode("userMobile")
                    .addConstraintViolation();
            isValid = false;
        } else if (userDto.getUserMobile().toString().length()!=10) {
            context.buildConstraintViolationWithTemplate("{invalid.mobile}")
                    .addPropertyNode("userMobile")
                    .addConstraintViolation();
            isValid = false;
        }
        if (userDto.getUserId()==null) {
            if (userDto.getUserPassword() == null) {
                context.buildConstraintViolationWithTemplate("{password.not.null}")
                        .addPropertyNode("userPassword")
                        .addConstraintViolation();
                isValid = false;
            } else if (userDto.getUserPassword().length()<7 && userDto.getUserPassword().length()>15) {
                context.buildConstraintViolationWithTemplate("{invalid.password}")
                        .addPropertyNode("userPassword")
                        .addConstraintViolation();
                isValid = false;
            }
        }
        if (userDto.getGender()==null) {
            context.buildConstraintViolationWithTemplate("{gender.not.null}")
                    .addPropertyNode("gender")
                    .addConstraintViolation();
            isValid = false;
        } else if (!gendersList.contains(userDto.getGender())) {
            context.buildConstraintViolationWithTemplate("{invalid.gender}")
                    .addPropertyNode("gender")
                    .addConstraintViolation();
            isValid = false;
        }
        if (userDto.getAddressDto()==null) {
            context.buildConstraintViolationWithTemplate("{address.not.null}")
                    .addPropertyNode("addressDto")
                    .addConstraintViolation();
            isValid = false;
        }
        if (userDto.getAddressDto()!=null && userDto.getAddressDto().getBillingAddress()!=null) {
            BillingAddress billingAddress = userDto.getAddressDto().getBillingAddress();
            if (billingAddress.getStreet()==null) {
                context.buildConstraintViolationWithTemplate("{billing.street.not.null}")
                        .addPropertyNode("addressDto.billingAddress.street")
                        .addConstraintViolation();
                isValid = false;
            }
            if (billingAddress.getCity()==null) {
                context.buildConstraintViolationWithTemplate("{billing.city.not.null}")
                        .addPropertyNode("addressDto.billingAddress.city")
                        .addConstraintViolation();
                isValid = false;
            }
            if (billingAddress.getState()==null) {
                context.buildConstraintViolationWithTemplate("{billing.state.not.null}")
                        .addPropertyNode("addressDto.billingAddress.state")
                        .addConstraintViolation();
                isValid = false;
            }
            if (billingAddress.getPinCode()==null) {
                context.buildConstraintViolationWithTemplate("{billing.pincode.not.null}")
                        .addPropertyNode("addressDto.billingAddress.pinCode")
                        .addConstraintViolation();
                isValid = false;
            }
        }
        if (userDto.getAddressDto()!=null && userDto.getAddressDto().getShippingAddress()!=null) {
            ShippingAddress shippingAddress = userDto.getAddressDto().getShippingAddress();
            if (shippingAddress.getStreet()==null) {
                context.buildConstraintViolationWithTemplate("{shipping.street.not.null}")
                        .addPropertyNode("addressDto.shippingAddress.street")
                        .addConstraintViolation();
                isValid = false;
            }
            if (shippingAddress.getCity()==null) {
                context.buildConstraintViolationWithTemplate("{shipping.city.not.null}")
                        .addPropertyNode("addressDto.shippingAddress.city")
                        .addConstraintViolation();
                isValid = false;
            }
            if (shippingAddress.getState()==null) {
                context.buildConstraintViolationWithTemplate("{shipping.state.not.null}")
                        .addPropertyNode("addressDto.shippingAddress.state")
                        .addConstraintViolation();
                isValid = false;
            }
            if (shippingAddress.getPinCode()==null) {
                context.buildConstraintViolationWithTemplate("{shipping.pincode.not.null}")
                        .addPropertyNode("addressDto.shippingAddress.pinCode")
                        .addConstraintViolation();
                isValid = false;
            }
        }
        return isValid;
    }
}

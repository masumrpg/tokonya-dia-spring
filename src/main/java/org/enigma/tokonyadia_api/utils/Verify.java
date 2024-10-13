package org.enigma.tokonyadia_api.utils;

import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.entity.Customer;
import org.enigma.tokonyadia_api.entity.UserAccount;
import org.enigma.tokonyadia_api.repository.CustomerRepository;
import org.enigma.tokonyadia_api.repository.UserAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public class Verify {
    public static void checkUserByUsername(String username, UserAccountRepository userAccountRepository) {
        Optional<UserAccount> optionalUserAccount = userAccountRepository.findByUsername(username);
        if (optionalUserAccount.isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, Constant.ERROR_USERNAME_DUPLICATE);
    }

    public static void checkCustomerByPhone(String phoneNumber, CustomerRepository customerRepository) {
        Optional<Customer> byPhone = customerRepository.findByPhoneNumber(phoneNumber);
        if (byPhone.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone with " + phoneNumber + " already exists!");
        }
    }

    public static void checkCustomerByEmail(String email, CustomerRepository customerRepository) {
        Optional<Customer> byEmail = customerRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email with " + email + " already exists!");
        }
    }
}

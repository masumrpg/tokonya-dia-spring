package org.enigma.tokonyadia_api.utils;

import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.repository.PersonRepository;
import org.enigma.tokonyadia_api.repository.StoreRepository;
import org.enigma.tokonyadia_api.repository.UserAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class Verify {
    public static void userByUsername(String username, UserAccountRepository userAccountRepository) {
        boolean existsByUsername = userAccountRepository.existsByUsername(username);
        if (existsByUsername) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, Constant.ERROR_USERNAME_DUPLICATE);
        }
    }

    public static void personByPhone(String phoneNumber, PersonRepository personRepository) {
        boolean existsByPhoneNumber = personRepository.existsByPhoneNumber(phoneNumber);
        if (existsByPhoneNumber) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone with " + phoneNumber + " already exists!");
        }
    }

    public static void personByEmail(String email, PersonRepository personRepository) {
        boolean existsByEmail = personRepository.existsByEmail(email);
        if (existsByEmail) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email with " + email + " already exists!");
        }
    }

    public static void storeBySiup(String siup, StoreRepository storeRepository) {
        boolean existsBySiup = storeRepository.existsBySiup(siup);
        if (existsBySiup) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Siup with " + siup + " already exist!");
        }
    }

    public static void storeByPhoneNumber(String phoneNumber, StoreRepository storeRepository) {
        boolean existsByPhoneNumber = storeRepository.existsByPhoneNumber(phoneNumber);
        if (existsByPhoneNumber) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone whith " + phoneNumber + " already exist!");
        }
    }
}

package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.UserRole;
import org.enigma.tokonyadia_api.entity.Person;
import org.enigma.tokonyadia_api.entity.UserAccount;
import org.enigma.tokonyadia_api.service.PersonService;
import org.enigma.tokonyadia_api.service.PermissionEvaluationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionEvaluationServiceImpl implements PermissionEvaluationService {
    private final PersonService personService;

    @Override
    public boolean hasAccessToCustomerAndSeller(String personId, String userId) {
        Person person = personService.getOneById(personId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        return !userAccount.getRole().equals(UserRole.ROLE_CUSTOMER) && !userAccount.getRole().equals(UserRole.ROLE_SELLER) || userAccount.getId().equals(person.getUserAccount().getId());
    }
}
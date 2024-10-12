package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.UserRole;
import org.enigma.tokonyadia_api.entity.Customer;
import org.enigma.tokonyadia_api.entity.UserAccount;
import org.enigma.tokonyadia_api.service.CustomerService;
import org.enigma.tokonyadia_api.service.PermissionEvaluationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionEvaluationServiceImpl implements PermissionEvaluationService {
    private final CustomerService customerService;

    @Override
    public boolean hasAccessToCustomer(String customerId, String userId) {
        Customer customer = customerService.getOneById(customerId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        return !userAccount.getRole().equals(UserRole.ROLE_CUSTOMER) || userAccount.getId().equals(customer.getUserAccount().getId());
    }
}
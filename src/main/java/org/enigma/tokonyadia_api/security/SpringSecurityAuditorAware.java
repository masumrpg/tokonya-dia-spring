package org.enigma.tokonyadia_api.security;

import lombok.NonNull;
import org.enigma.tokonyadia_api.entity.UserAccount;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<String> {
    @NonNull
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.of("System");
        }

        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        return Optional.ofNullable(userAccount.getCustomer().getName());
    }
}

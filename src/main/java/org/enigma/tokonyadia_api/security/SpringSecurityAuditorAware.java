package org.enigma.tokonyadia_api.security;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.enigma.tokonyadia_api.entity.UserAccount;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
public class SpringSecurityAuditorAware implements AuditorAware<String> {
    @NonNull
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.of("System");
        }

        return Optional.ofNullable(((UserAccount) authentication.getPrincipal()).getCustomer().getName());

    }
}

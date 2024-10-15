package org.enigma.tokonyadia_api.config;

import org.enigma.tokonyadia_api.security.SecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new SecurityAuditorAware();
    }
}

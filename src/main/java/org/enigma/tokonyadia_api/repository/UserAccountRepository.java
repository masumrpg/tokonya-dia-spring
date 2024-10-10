package org.enigma.tokonyadia_api.repository;

import org.enigma.tokonyadia_api.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, String>, JpaSpecificationExecutor<UserAccount> {
    Optional<UserAccount> findByUsername(String username);
}

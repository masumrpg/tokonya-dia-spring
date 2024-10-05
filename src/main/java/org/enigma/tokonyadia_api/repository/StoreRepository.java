package org.enigma.tokonyadia_api.repository;

import org.enigma.tokonyadia_api.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, String>, JpaSpecificationExecutor<Store> {
    Optional<Store> findBySiup(String siup);
    Optional<Store> findByPhoneNumber(String phoneNumber);
}

package org.enigma.tokonyadia_api.repository;

import org.enigma.tokonyadia_api.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface StoreRepository extends JpaRepository<Store, String>, JpaSpecificationExecutor<Store> {
    boolean existsBySiup(String siup);
    boolean existsByPhoneNumber(String phoneNumber);
}

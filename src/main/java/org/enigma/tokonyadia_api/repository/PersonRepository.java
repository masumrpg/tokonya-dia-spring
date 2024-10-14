package org.enigma.tokonyadia_api.repository;

import org.enigma.tokonyadia_api.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, String>, JpaSpecificationExecutor<Person> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phone);
}

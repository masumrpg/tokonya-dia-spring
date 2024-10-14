package org.enigma.tokonyadia_api.service;

import org.enigma.tokonyadia_api.dto.request.RegisterCreateRequest;
import org.enigma.tokonyadia_api.dto.request.PersonRequest;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.request.UserRequest;
import org.enigma.tokonyadia_api.dto.response.PersonResponse;
import org.enigma.tokonyadia_api.entity.Person;
import org.springframework.data.domain.Page;

public interface PersonService {
    PersonResponse create(RegisterCreateRequest request);
    PersonResponse create(UserRequest request);
    Person create(Person person);
    Person getOneById(String id);
    PersonResponse getById(String id);
    PersonResponse update(String id, PersonRequest personRequest);
    void delete(String id);
    Page<PersonResponse> getAll(SearchCommonRequest request);
}

package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.constant.Gender;
import org.enigma.tokonyadia_api.constant.UserRole;
import org.enigma.tokonyadia_api.dto.request.UpdatePersonRequest;
import org.enigma.tokonyadia_api.dto.request.RegisterCreateRequest;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.response.PersonResponse;
import org.enigma.tokonyadia_api.entity.Person;
import org.enigma.tokonyadia_api.entity.UserAccount;
import org.enigma.tokonyadia_api.repository.PersonRepository;
import org.enigma.tokonyadia_api.service.PersonService;
import org.enigma.tokonyadia_api.service.UserAccountService;
import org.enigma.tokonyadia_api.specification.FilterSpecificationBuilder;
import org.enigma.tokonyadia_api.util.MapperUtil;
import org.enigma.tokonyadia_api.util.SortUtil;
import org.enigma.tokonyadia_api.util.Verify;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.enigma.tokonyadia_api.util.MapperUtil.*;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final UserAccountService userAccountService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PersonResponse create(RegisterCreateRequest request) {
        Verify.personByEmail(request.getEmail(), personRepository);
        Verify.personByPhone(request.getPhoneNumber(), personRepository);

        if (request.getRole().equals(UserRole.ROLE_ADMIN.getDescription())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }

        UserAccount userAccount = UserAccount.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .role(UserRole.findByDescription(request.getRole()))
                .build();
        userAccountService.create(userAccount);
        Person person = Person.builder()
                .name(request.getName())
                .gender(Gender.findByDescription(request.getGender()))
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .userAccount(userAccount)
                .build();
        personRepository.saveAndFlush(person);
        return toPersonResponse(person);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Person create(Person person) {
        Verify.personByEmail(person.getEmail(), personRepository);
        Verify.personByPhone(person.getPhoneNumber(), personRepository);

        personRepository.saveAndFlush(person);
        return person;
    }

    @Transactional(readOnly = true)
    @Override
    public Person getOneById(String id) {
        Optional<Person> byId = personRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_PERSON_NOT_FOUND);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PersonResponse getById(String id) {
        return toPersonResponse(getOneById(id));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PersonResponse update(String id, UpdatePersonRequest request) {
        Verify.personByPhone(request.getPhoneNumber(), personRepository);
        Verify.personByEmail(request.getEmail(), personRepository);

        Person person = getOneById(id);
        person.setName(request.getName());
        person.setImgUrl(request.getImgUrl());
        person.setGender(Gender.findByDescription(request.getGender()));
        person.setEmail(request.getEmail());
        person.setPhoneNumber(request.getPhoneNumber());
        person.setAddress(request.getAddress());
        personRepository.save(person);
        return toPersonResponse(person);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        Person person = getOneById(id);
        personRepository.delete(person);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PersonResponse> getAll(SearchCommonRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Person> specification = new FilterSpecificationBuilder<Person>()
                .withLike("name", request.getQuery())
                .withEqual("phoneNumber", request.getQuery())
                .build();
        Page<Person> resultPage = personRepository.findAll(specification, pageable);

        return resultPage.map(MapperUtil::toPersonResponse);
    }
}

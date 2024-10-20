package org.enigma.tokonyadia_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.constant.Gender;
import org.enigma.tokonyadia_api.constant.UserRole;
import org.enigma.tokonyadia_api.dto.request.RegisterCreateRequest;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.request.UpdatePersonRequest;
import org.enigma.tokonyadia_api.dto.response.PersonResponse;
import org.enigma.tokonyadia_api.entity.Person;
import org.enigma.tokonyadia_api.entity.UserAccount;
import org.enigma.tokonyadia_api.repository.PersonRepository;
import org.enigma.tokonyadia_api.service.PersonService;
import org.enigma.tokonyadia_api.service.UserAccountService;
import org.enigma.tokonyadia_api.specification.FilterSpecificationBuilder;
import org.enigma.tokonyadia_api.util.MapperUtil;
import org.enigma.tokonyadia_api.util.SortUtil;
import org.enigma.tokonyadia_api.util.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.enigma.tokonyadia_api.util.MapperUtil.toPersonResponse;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final UserAccountService userAccountService;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PersonResponse create(RegisterCreateRequest request) {
        validationUtil.validate(request);
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
                .address(request.getAddress())
                .userAccount(userAccount)
                .build();
        personRepository.saveAndFlush(person);
        return toPersonResponse(person);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Person create(Person person) {
        validationUtil.validate(person);
        personRepository.saveAndFlush(person);
        return person;
    }

    @Transactional(readOnly = true)
    @Override
    public Person getOne(String id) {
        return personRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_PERSON_NOT_FOUND));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PersonResponse getById(String id) {
        return toPersonResponse(getOne(id));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PersonResponse update(String id, UpdatePersonRequest request) {
        validationUtil.validate(request);
        Person person = getOne(id);
        person.setName(request.getName());
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
        Person person = getOne(id);
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

package org.enigma.tokonyadia_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.dto.request.RegisterCreateRequest;
import org.enigma.tokonyadia_api.dto.request.UpdatePersonRequest;
import org.enigma.tokonyadia_api.dto.request.SearchCommonRequest;
import org.enigma.tokonyadia_api.dto.response.PersonResponse;
import org.enigma.tokonyadia_api.service.PersonService;
import org.enigma.tokonyadia_api.util.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.PERSON_API)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Person Management", description = "APIs for managing persons")
public class PersonController {
    private final PersonService personService;


    @Operation(summary = "Create a new person", description = "Create a new person with the provided details")
    @PostMapping
    public ResponseEntity<?> createPerson(@RequestBody RegisterCreateRequest request) {
        PersonResponse personResponse = personService.create(request);
        return ResponseUtil.buildCommonResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATED_PERSON, personResponse);
    }

    @Operation(summary = "Get a person by ID", description = "Retrieve the details of a person by their ID")
    @GetMapping(path = "/{personId}")
    public ResponseEntity<?> getPersonById(@PathVariable String personId) {
        PersonResponse personResponse = personService.getById(personId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_GET_PERSON, personResponse);
    }

    @PreAuthorize("hasRole('ADMIN') or ((hasRole('SELLER') or hasRole('CUSTOMER')) and @permissionEvaluationServiceImpl.hasAccessToCustomerAndSeller(#personId, authentication.principal.id))")
    @Operation(summary = "Update a person", description = "Update the details of an existing person by their ID")
    @PutMapping("/{personId}")
    public ResponseEntity<?> updatePerson(@PathVariable String personId, @RequestBody UpdatePersonRequest request) {
        PersonResponse personResponse = personService.update(personId, request);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_PERSON, personResponse);
    }

    @Operation(summary = "Delete a person", description = "Delete a person by their ID")
    @DeleteMapping("/{personId}")
    public ResponseEntity<?> deletePerson(@PathVariable String personId) {
        personService.delete(personId);
        return ResponseUtil.buildCommonResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_PERSON, null);
    }

    @Operation(summary = "Get all persons", description = "Retrieve a paginated list of persons with optional filtering")
    @GetMapping
    public ResponseEntity<?> getAllPersons(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "q", required = false) String query
    ) {
        SearchCommonRequest searchCommonRequest = SearchCommonRequest.builder()
                .size(size)
                .page(page)
                .sortBy(sort)
                .query(query)
                .build();
        Page<PersonResponse> personResponsePage = personService.getAll(searchCommonRequest);
        return ResponseUtil.buildResponsePage(HttpStatus.OK, Constant.SUCCESS_GET_ALL_PERSON, personResponsePage);
    }
}

package org.enigma.tokonyadia_api.service.impl;

import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.entity.Person;
import org.enigma.tokonyadia_api.entity.Store;
import org.enigma.tokonyadia_api.repository.StoreRepository;
import org.enigma.tokonyadia_api.service.PersonService;
import org.enigma.tokonyadia_api.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StoreServiceImplTest {

    @InjectMocks
    private StoreServiceImpl storeService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private PersonService personService;

    @Mock
    private ValidationUtil validationUtil;

    private Person person;
    private Store store;

    @BeforeEach
    void setUp() {
        person = new Person(); // Initialize as needed
        store = new Store(); // Initialize as needed
    }

//    @Test
//    void testCreateStore() {
//        StoreRequest request = new StoreRequest();
//        request.setName("Test Store");
//        request.setPersonId("1");
//        request.setPhoneNumber("123456789");
//        request.setSiup("SIUP123");
//        request.setAddress("Test Address");
//
//        when(personService.getOne(anyString())).thenReturn(person);
//        when(storeRepository.saveAndFlush(any(Store.class))).thenReturn(store);
//        when(MapperUtil.toStoreResponse(any(Store.class))).thenReturn(new StoreResponse());
//
//        StoreResponse response = storeService.create(request);
//
//        assertNotNull(response);
//        verify(validationUtil).validate(request);
//        verify(storeRepository).saveAndFlush(any(Store.class));
//    }

//    @Test
//    void testGetById() {
//        when(storeRepository.findById(anyString())).thenReturn(java.util.Optional.of(store));
//        when(MapperUtil.toStoreResponse(any(Store.class))).thenReturn(new StoreResponse());
//
//        StoreResponse response = storeService.getById("1");
//
//        assertNotNull(response);
//        verify(storeRepository).findById("1");
//    }

//    @Test
//    void testUpdateStore() {
//        StoreRequest request = new StoreRequest();
//        request.setName("Updated Store");
//        request.setPhoneNumber("987654321");
//        request.setSiup("SIUP456");
//        request.setAddress("Updated Address");
//
//        when(storeRepository.findById(anyString())).thenReturn(java.util.Optional.of(store));
//        when(storeRepository.save(any(Store.class))).thenReturn(store);
//        when(MapperUtil.toStoreResponse(any(Store.class))).thenReturn(new StoreResponse());
//
//        StoreResponse response = storeService.update("1", request);
//
//        assertNotNull(response);
//        verify(validationUtil).validate(request);
//        verify(storeRepository).save(any(Store.class));
//    }

    @Test
    void testDeleteStore() {
        when(storeRepository.findById(anyString())).thenReturn(java.util.Optional.of(store));

        storeService.delete("1");

        verify(storeRepository).delete(store);
    }

    @Test
    void testGetOneStoreNotFound() {
        when(storeRepository.findById(anyString())).thenReturn(java.util.Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            storeService.getOne("1");
        });

        assertEquals(HttpStatus.NOT_FOUND, ((ResponseStatusException) exception).getStatusCode());
        assertEquals(Constant.ERROR_STORE_NOT_FOUND, ((ResponseStatusException) exception).getReason());
    }
}
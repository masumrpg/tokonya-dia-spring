package org.enigma.tokonyadia_api.service.impl;

import org.enigma.tokonyadia_api.entity.Store;
import org.enigma.tokonyadia_api.repository.StoreRepository;
import org.enigma.tokonyadia_api.service.StoreService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;

    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public Store create(Store store) {
        Optional<Store> bySiup = storeRepository.findBySiup(store.getSiup());
        if (bySiup.isPresent()) {
            throw new RuntimeException("Siup sudah ada!");
        }
        Optional<Store> byPhoneNumber = storeRepository.findByPhoneNumber(store.getPhoneNumber());
        if (byPhoneNumber.isPresent()) {
            throw new RuntimeException("Phone number sudah ada!");
        }
        return storeRepository.save(store);
    }

    @Override
    public Store getById(String id) {
        Optional<Store> byId = storeRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        throw new RuntimeException("Toko tidak ditemukan!");
    }

    @Override
    public Store update(String id, Store store) {
        Optional<Store> byId = storeRepository.findById(id);
        if (byId.isPresent()) {
            Store updateStore = byId.get();
            updateStore.setSiup(store.getSiup());
            updateStore.setName(store.getName());
            updateStore.setPhoneNumber(store.getPhoneNumber());
            updateStore.setAddress(store.getAddress());
            return storeRepository.save(updateStore);
        }
        throw new RuntimeException("Toko tidak ditemukan!");
    }

    @Override
    public String delete(String id) {
        Optional<Store> byId = storeRepository.findById(id);
        if (byId.isPresent()) {
            storeRepository.delete(byId.get());
            return "Toko dengan id " + id + " telah dihapus!";
        }
        return "Toko tidak ditemukan!";
    }

    @Override
    public List<Store> getAll() {
        List<Store> storeList = storeRepository.findAll();
        if (storeList.isEmpty()) {
            throw new RuntimeException("Toko masih kosong!");
        }
        return storeList;
    }
}

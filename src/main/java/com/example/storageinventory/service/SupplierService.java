package com.example.storageinventory.service;

import com.example.storageinventory.model.Supplier;
import com.example.storageinventory.repository.SupplierRepository;
import java.util.List;

public class SupplierService {

    private final SupplierRepository repository = new SupplierRepository();

    public void saveSupplier(Supplier supplier) {
        repository.save(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        return repository.getAll();
    }

    public void deleteSupplier(Supplier supplier) {
        repository.delete(supplier);
    }
}
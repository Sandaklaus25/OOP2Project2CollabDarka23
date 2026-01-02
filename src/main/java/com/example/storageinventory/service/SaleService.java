package com.example.storageinventory.service;

import com.example.storageinventory.model.Sale;
import com.example.storageinventory.repository.SaleRepository;
import java.util.List;

public class SaleService {

    private final SaleRepository repository = new SaleRepository();

    public void createSale(Sale sale) throws Exception {
        repository.save(sale);
    }

    public List<Sale> getAllSales() {
        return repository.getAll();
    }
}
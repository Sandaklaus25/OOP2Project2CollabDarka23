package com.example.storageinventory.service;

import com.example.storageinventory.model.Product;
import com.example.storageinventory.repository.ProductRepository;

import java.util.List;

public class ProductService {
    private final ProductRepository repository = new ProductRepository();

    public List<Product> getAllProducts() {
        return repository.getAll();
    }

    public void saveProduct(Product product) {
        repository.save(product);
    }

    public void deleteProduct(Product product) {
        repository.delete(product);
    }
}
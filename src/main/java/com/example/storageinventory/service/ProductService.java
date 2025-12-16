package com.example.storageinventory.service;

import com.example.storageinventory.model.Product;
import com.example.storageinventory.repository.ProductRepository;
import java.util.List;

public class ProductService {
    private final ProductRepository repository = new ProductRepository();

    public List<Product> getAllProducts() {
        return repository.getAll();
    }

    public void saveProduct(String name, Double deliveryPrice, Double salePrice, Integer criticalMin) {
        // При създаване на нова стока, количеството обикновено е 0
        Product product = new Product(name, 0, deliveryPrice, salePrice, criticalMin);
        repository.save(product);
    }

    public void deleteProduct(Product product) {
        repository.delete(product);
    }
}
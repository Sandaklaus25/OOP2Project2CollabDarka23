package com.example.storageinventory.service;

import com.example.storageinventory.model.Delivery;
import com.example.storageinventory.repository.DeliveryRepository;
import java.util.List;

public class DeliveryService {

    private final DeliveryRepository repository = new DeliveryRepository();

    // Този метод хвърля Exception, ако няма пари. Контролерът ще го улови.
    public void createDelivery(Delivery delivery) throws Exception {
        repository.save(delivery);
    }

    public List<Delivery> getAllDeliveries() {
        return repository.getAll();
    }
}
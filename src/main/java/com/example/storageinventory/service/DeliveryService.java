package com.example.storageinventory.service;

import com.example.storageinventory.model.Delivery;
import com.example.storageinventory.repository.DeliveryRepository;
import com.example.storageinventory.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
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

    public List<Delivery> getDeliveriesByPeriod(LocalDate startDate, LocalDate endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Delivery d WHERE d.deliveryDate BETWEEN :start AND :end";

            Query<Delivery> query = session.createQuery(hql, Delivery.class);
            query.setParameter("start", startDate);
            query.setParameter("end", endDate);

            return query.list();
        }
    }
}
package com.example.storageinventory.service;

import com.example.storageinventory.model.Sale;
import com.example.storageinventory.repository.SaleRepository;
import com.example.storageinventory.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class SaleService {

    private final SaleRepository repository = new SaleRepository();

    public void createSale(Sale sale) throws Exception {
        repository.save(sale);
    }

    public List<Sale> getAllSales() {
        return repository.getAll();
    }

    public List<Sale> getSalesByPeriod(LocalDate startDate, LocalDate endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "FROM Sale s WHERE s.saleDate BETWEEN :start AND :end";

            Query<Sale> query = session.createQuery(hql, Sale.class);
            query.setParameter("start", startDate);
            query.setParameter("end", endDate);

            return query.list();
        }
    }
}
package com.example.storageinventory.service;

import com.example.storageinventory.model.CashRegister;
import com.example.storageinventory.util.HibernateUtil;
import org.hibernate.Session;

public class CashRegisterService {

    public Double getCurrentBalance() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CashRegister reg = session.get(CashRegister.class, 1L);
            if (reg != null) {
                return reg.getBalance();
            }
            return 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}
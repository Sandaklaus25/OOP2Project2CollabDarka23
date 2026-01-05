package com.example.storageinventory.service;

import com.example.storageinventory.model.CashRegister;
import com.example.storageinventory.util.HibernateUtil;
import org.hibernate.Session;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CashRegisterService {

    private static final Logger logger = Logger.getLogger(CashRegisterService.class.getName());

    public Double getCurrentBalance() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CashRegister reg = session.get(CashRegister.class, 1L);
            if (reg != null) {
                return reg.getBalance();
            }
            return 0.0;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Грешка при зареждане на каса/баланс!", e);
            return 0.0;
        }
    }
}
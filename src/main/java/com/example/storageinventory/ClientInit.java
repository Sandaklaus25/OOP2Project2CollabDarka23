package com.example.storageinventory;

import com.example.storageinventory.model.Client;
import com.example.storageinventory.repository.ClientRepository;
import com.example.storageinventory.util.HibernateUtil;

public class ClientInit {
    public static void main(String[] args) {
        System.out.println("⏳ Добавяне на клиенти...");
        ClientRepository repo = new ClientRepository();

        try {
            Client c1 = new Client("Иван Петров", "9010101234", "София, Младост 1", "0888123456");
            Client c2 = new Client("Фирма ЕООД", "202020202", "Пловдив, Център", "0899123456");

            repo.save(c1);
            repo.save(c2);

            System.out.println("✅ Клиентите са добавени успешно!");
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
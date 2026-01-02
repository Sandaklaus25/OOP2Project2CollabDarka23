package com.example.storageinventory.service;

import com.example.storageinventory.model.Client;
import com.example.storageinventory.repository.ClientRepository;
import java.util.List;

public class ClientService {

    private final ClientRepository repository = new ClientRepository();

    public void saveClient(Client client) {
        repository.save(client);
    }

    public List<Client> getAllClients() {
        return repository.getAll();
    }

    public void deleteClient(Client client) {
        repository.delete(client);
    }
}
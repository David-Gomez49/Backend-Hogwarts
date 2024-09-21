package com.api.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.backend.model.ClientModel;
import com.api.backend.repository.ClientRepo;

@Service
public class ClientService {

    @Autowired
    private ClientRepo clientRepo;

    public List<ClientModel> obtainClientList() {
        return (List<ClientModel>) clientRepo.findAll();
    }

    public ClientModel createClient(ClientModel client) {
        return clientRepo.save(client);
    }

    public ClientModel updateClient(ClientModel client) {
        return clientRepo.save(client);
    }

    public void deleteClient(int id) {
        clientRepo.deleteById(id);
    }
}

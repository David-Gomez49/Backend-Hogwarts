package com.api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.ClientModel;
import com.api.backend.services.ClientService;

@RestController
@RequestMapping("/client")
public class ClientControl {

    @Autowired
    private ClientService clientService;

    @GetMapping("/getAll")
    public List<ClientModel> obtainClientList() {
        return clientService.obtainClientList();
    }

    @PostMapping("/create")
    public ClientModel createClient(@RequestBody ClientModel client) {
        return clientService.createClient(client);
    }

    @PutMapping("/edit/{id}")
    public ClientModel updateClient(@PathVariable int id, @RequestBody ClientModel client) {
        client.setId(id);
        return clientService.updateClient(client);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteClient(@PathVariable int id) {
        clientService.deleteClient(id);
    }
}

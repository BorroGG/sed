package com.borrogg.service;

import com.borrogg.entities.Client;
import com.borrogg.entities.Department;
import com.borrogg.enums.Position;
import com.borrogg.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public void addClient(Client client) {
        clientRepository.save(client);
    }

    public boolean isDepartmentHasABoss(Department department) {
        List<Client> clients = clientRepository.findAllByDepartmentAndPosition(department, Position.BOSS);
        return clients != null;
    }

    public Client getClient(Integer id) {
        return clientRepository.findById(id).get();
    }

    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    public void removeClient(Client client) {
        clientRepository.delete(client);
    }

    public List<Client> findByFIOPattern(String query) {
        return clientRepository.findAllByFIOContaining(query);
    }

    public List<Client> findByPhonePattern(String query) {
        return clientRepository.findAllByPhoneContaining(query);
    }

    public void updateClient(Client client) {
        clientRepository.save(client);
    }
}

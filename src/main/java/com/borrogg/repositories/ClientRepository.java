package com.borrogg.repositories;

import com.borrogg.entities.Client;
import com.borrogg.entities.Department;
import com.borrogg.enums.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    List<Client> findAllByDepartmentAndPosition(Department department, Position position);
    List<Client> findAllByFIOContaining(String query);
    List<Client> findAllByPhoneContaining(String query);
}

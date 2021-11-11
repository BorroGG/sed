package com.borrogg.repositories;

import com.borrogg.entities.Client;
import com.borrogg.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Integer> {
    List<File> findAllByClient(Client client);
    List<File> findAllByFormatAndSizeKBBetween(String format, Integer from, Integer to);
}

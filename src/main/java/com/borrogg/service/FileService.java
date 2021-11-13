package com.borrogg.service;

import com.borrogg.entities.Client;
import com.borrogg.entities.File;
import com.borrogg.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void addFile(File file) {
        fileRepository.save(file);
    }

    public List<File> getFilesByClientId(Client client) {
        return fileRepository.findAllByClient(client);
    }

    public File getFileById(Integer id) {
        return fileRepository.findById(id).get();
    }

    public List<File> findAllByFormatAndSizeKBBetween(String format, Integer from, Integer to) {
        return fileRepository.findAllByFormatAndSizeKBBetween(format, from, to);
    }

    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

    public boolean isLastFileInStorage(String fileName) {
        return fileRepository.countFileByName(fileName) == 1;
    }

    public void deleteFile(File file) {
        fileRepository.delete(file);
    }
}

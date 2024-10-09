package com.restApi.file.service.rest.api.repository;

import com.restApi.file.service.rest.api.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData, Long> {
   Optional<FileData> findByName(String fileName);
}

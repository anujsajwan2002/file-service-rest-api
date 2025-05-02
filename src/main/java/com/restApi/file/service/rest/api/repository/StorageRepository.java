package com.restApi.file.service.rest.api.repository;

import com.restApi.file.service.rest.api.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StorageRepository extends JpaRepository<ImageData,Long> {


    List<ImageData> findByName(String name);

}

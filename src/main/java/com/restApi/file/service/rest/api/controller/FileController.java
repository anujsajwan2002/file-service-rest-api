package com.restApi.file.service.rest.api.controller;

import com.restApi.file.service.rest.api.entity.FileData;
import com.restApi.file.service.rest.api.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/image")
public class FileController {

    @Autowired
    private StorageService service;

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam("file")MultipartFile file) throws IOException {
        String uploadedFile = service.uploadFile(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadedFile);
    }

    @PostMapping("/fileSystem")
    public ResponseEntity<?> uploadFileToFileSystem(@RequestParam("file")MultipartFile file) throws IOException{
        String uploadedFile = service.uploadFileToFileSystem(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadedFile);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName) throws IOException {
        byte[] fileData=service.downloadFile(fileName);
        String fileType=service.getFileType(fileName);

        if(fileType == null){
            fileType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(fileType))
                .body(fileData);
    }

    @GetMapping("/fileSystem/{fileName}")
    public ResponseEntity<?> downloadFileFromFileSystem(@PathVariable String fileName) throws IOException{
        byte[] fileData=service.downloadFileFromFileSystem(fileName);
        Path path = Paths.get("D:\\JAVA practice\\files_FileService\\", fileName);
        String fileType = Files.probeContentType(path);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(fileType != null ? fileType : MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .body(fileData);
    }
}

package com.restApi.file.service.rest.api.service;

import com.restApi.file.service.rest.api.entity.FileData;
import com.restApi.file.service.rest.api.entity.ImageData;
import com.restApi.file.service.rest.api.repository.FileDataRepository;
import com.restApi.file.service.rest.api.repository.StorageRepository;
import com.restApi.file.service.rest.api.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class StorageService {
    @Autowired
    private StorageRepository repository;

    @Autowired
    private FileDataRepository fileDataRepository;

    private final String FOLDER_PATH = "D:\\JAVA practice\\files_FileService\\";

    public String uploadFile(MultipartFile file)throws IOException{

        ImageData imageData = repository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes())).build());
        if (imageData != null) {
            return "File Uploaded Successfully" + file.getOriginalFilename();
        }
        return null;
    }

    public byte[] downloadFile(String fileName){
        Optional<ImageData> dbFileData = repository.findByName(fileName);

        if(dbFileData.isPresent()){
            return ImageUtils.decompressImage(dbFileData.get().getImageData());
        }
        return null;
    }

    public String uploadFileToFileSystem(MultipartFile file) throws IOException{
        String filePath=FOLDER_PATH+file.getOriginalFilename();

        FileData fileData = fileDataRepository.save(FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath).build());

        file.transferTo(new File(filePath));

        if (fileData != null) {
            return "File Uploaded Successfully : " + filePath;
        }
         return null ;
    }

    public byte[] downloadFileFromFileSystem(String fileName) throws IOException {
        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
        if(fileData.isPresent()) {
            String filePath = fileData.get().getFilePath();
            Path path = Paths.get(filePath);
            return Files.readAllBytes(path);
        }
        return null;
    }

    public String getFileType(String fileName) throws IOException {
        Optional<FileData> fileData =fileDataRepository.findByName(fileName);

        if(fileData.isPresent()){
            Path filePath =Paths.get(fileData.get().getFilePath());
            String fileType = Files.probeContentType(filePath);
            return (fileType != null) ? fileType : MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        return null;
    }
}

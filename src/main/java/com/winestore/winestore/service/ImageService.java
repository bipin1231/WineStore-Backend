package com.winestore.winestore.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {
    private final  String folder_path="C://Users//bipin//Downloads//winestore-images/";

    public String addImage(MultipartFile image) throws IOException {
        String fileName= UUID.randomUUID()+"_"+image.getOriginalFilename();
        Path filePath= Paths.get(folder_path+fileName);
        Files.copy(image.getInputStream(),filePath);
        return fileName;
    }

    public List<String> addMultipleImage(List<MultipartFile> images) throws IOException {
        List<String> imageFileName=new ArrayList<>();
        for (MultipartFile file:images){
            String fileName= UUID.randomUUID()+"_"+file.getOriginalFilename();
            Path filePath= Paths.get(folder_path+fileName);
            Files.copy(file.getInputStream(),filePath);
            imageFileName.add(fileName);
        }
        return imageFileName;
    }
    public boolean deleteImage(String fileName) {
        try {
            Path filePath = Paths.get(folder_path + fileName);
            return Files.deleteIfExists(filePath);  // Returns true if file deleted, false if file didn't exist
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}

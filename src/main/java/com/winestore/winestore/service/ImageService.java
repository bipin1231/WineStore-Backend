package com.winestore.winestore.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ImageService {

    private final Cloudinary cloudinary;

    public ImageService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    // Upload a single image
    public String addImage(MultipartFile image) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                ObjectUtils.asMap("folder", "winestore_uploads"));
        return uploadResult.get("secure_url").toString(); // return Cloudinary URL
    }

    // Upload multiple images
    public List<String> addMultipleImage(List<MultipartFile> images) throws IOException {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : images) {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "winestore_uploads"));
            imageUrls.add(uploadResult.get("secure_url").toString());
        }
        return imageUrls;
    }

    // Delete image by public_id (not filename anymore!)
    public boolean deleteImage(String publicId) {
        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(result.get("result")); // returns true if deleted
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

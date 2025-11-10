package com.burgerstream.backend.component;

import com.burgerstream.backend.model.menu.MenuItem;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class MenuItemHelper {
    public void validate(MenuItem menuItem){
        if (menuItem.getName() == null || menuItem.getName().isBlank()){
            throw new IllegalArgumentException("Menu item must have a name");
        }
        if (menuItem.getBasePrice() == null){
            throw new IllegalArgumentException("Menu item must have a base price");
        }
    }

    public void saveImage(MultipartFile image){
        if (image == null || image.isEmpty()) {
            return;
        }

        try{
            String filename = image.getOriginalFilename();

            Path uploadDir = Paths.get("burgerstream-backend/uploads/images").toAbsolutePath().normalize();
            Files.createDirectories(uploadDir);
            Path imagePath = uploadDir.resolve(filename);
            Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("✅ Image saved to: " + imagePath);
        } catch (IOException e){
            throw new RuntimeException("Failed to save image file",e);
        }
    }

    public void deleteImage(String imageUrl){
        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }

        try{
            Path imageDir = Paths.get("burgerstream-backend/uploads/images").toAbsolutePath().normalize();
            Path imagePath = imageDir.resolve(imageUrl);
            Files.deleteIfExists(imagePath);
        } catch (IOException e){
            throw new RuntimeException("Failed to delete image file", e);
        }
    }

    public void replaceImage(String oldImageUrl, MultipartFile newImage){
        if (newImage == null || newImage.isEmpty()) {
            return;
        }

        deleteImage(oldImageUrl);
        saveImage(newImage);
    }
}
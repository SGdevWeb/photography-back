package org.photography.api.service;

import org.photography.api.dto.PhotoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

@Service
public class PhotoService {

    @Value("${upload.path}")
    private String uploadPath;

    public String uploadPhoto(PhotoDTO photoDTO){
        MultipartFile file = photoDTO.getPhoto();
        String contentType = photoDTO.getContentType();

        System.out.println(file);
        System.out.println(contentType);

        String originalFileName = file.getOriginalFilename();

        File[] files = new File(uploadPath + "/" + contentType).listFiles();
        if (files != null) {
            boolean fileExists = Arrays.stream(files)
                    .map(File::getName)
                    .anyMatch(fileName -> fileName.endsWith("-" + originalFileName));

            if (fileExists) {
                throw new RuntimeException("Un fichier avec le même nom existe déjà.");
            }
        }

        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

        String subDirectory = contentType;
        Path targetLocation = Path.of(uploadPath + "/" + subDirectory + "/" + fileName);
        try {
            Files.createDirectories(targetLocation.getParent());

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String photoUrl = "/uploads/" + subDirectory + "/" + fileName;

            System.out.println(photoUrl);

            return photoUrl;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la copie du fichier sur le serveur");
        }
    }

    public byte[] getMediaContent(String contentType, String fileName) throws IOException {
        Path filePath = Path.of(uploadPath, contentType, fileName);

        return Files.readAllBytes(filePath);
    }

    public void deletePhoto(String contentType, String fileName) {
        System.out.println("Suppression");
        Path filePath = Path.of(uploadPath, contentType, fileName);
        System.out.println(filePath);
        try {
            Files.deleteIfExists(filePath);
            System.out.println("fichier supprimé !");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression du fichier sur le serveur");
        }
    }

    public void updatePhoto(String contentType, String oldFileName, String newFileName) {
        Path oldFilePath = Path.of(uploadPath, contentType, oldFileName);
        Path newFilePath = Path.of(uploadPath, contentType, newFileName);

        System.out.println(oldFilePath);
        System.out.println(newFilePath);
        try {
            Files.move(oldFilePath, newFilePath);
//            deletePhoto(contentType, oldFileName);
            System.out.println("Fichier renommé avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du  renommage du fichier sur le serveur");
        }
    }

}

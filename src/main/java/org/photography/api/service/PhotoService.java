package org.photography.api.service;

import org.photography.api.dto.PhotoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class PhotoService {

    @Value("${upload.path}")
    private String uploadPath;

    public String uploadPhoto(PhotoDTO photoDTO){
        MultipartFile file = photoDTO.getPhoto();
        String contentType = photoDTO.getContentType();

        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

        String subDirectory = contentType;
        Path targetLocation = Path.of(uploadPath + "/" + subDirectory + "/" + fileName);
        try {
            Files.createDirectories(targetLocation.getParent());

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String photoUrl = "/uploads/" + subDirectory + "/" + fileName;

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
        Path filePath = Path.of(uploadPath, contentType, fileName);
        try {
            Files.deleteIfExists(filePath);
            System.out.println("fichier supprimé !");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression du fichier sur le serveur");
        }
    }

}

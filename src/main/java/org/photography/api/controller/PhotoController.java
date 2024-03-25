package org.photography.api.controller;

import org.photography.api.dto.PhotoDTO;
import org.photography.api.exception.FileCopyException;
import org.photography.api.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/uploads")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @PostMapping("upload-photo")
    public ResponseEntity<String> fileUpload(@RequestParam("photo") MultipartFile photo,
                                             @RequestParam("contentType") String contentType) {
        try {
            PhotoDTO photoDTO = new PhotoDTO();
            photoDTO.setPhoto(photo);
            photoDTO.setContentType(contentType);
            String imageUrl = photoService.uploadPhoto(photoDTO);
            return new ResponseEntity<>(imageUrl, HttpStatus.OK);
        } catch (FileCopyException e) {
            return new ResponseEntity<>("Erreur lors de la copie du fichier sur le serveur", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de l'enregistrement du fichier", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{contentType}/{fileName}")
    public ResponseEntity<?> serveImage(@PathVariable String contentType, @PathVariable String fileName) {
        try {
            byte[] mediaContent = photoService.getMediaContent(contentType, fileName);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(mediaContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Erreur lors de la récupération du fichier : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{contentType}/{fileName}")
    public ResponseEntity<String> deletePhoto(@PathVariable String contentType, @PathVariable String fileName) {
        photoService.deletePhoto(contentType, fileName);

        return new ResponseEntity<>("Photo supprimée avec succés", HttpStatus.OK);
    }

}

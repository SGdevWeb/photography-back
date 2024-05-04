package org.photography.api.controller;

import org.photography.api.dto.ResetPasswordDTO;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.model.User;
import org.photography.api.repository.UserRepository;
import org.photography.api.service.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Map;

@RestController
@RequestMapping("/api/password")
public class PasswordController {

    private final UserRepository userRepository;

    private PasswordService passwordService;

    private static final Logger logger = LoggerFactory.getLogger(PasswordController.class);

    public PasswordController(PasswordService passwordService, UserRepository userRepository) {
        this.passwordService = passwordService;
        this.userRepository = userRepository;
    }

    @PostMapping("/reset")
    public ResponseEntity<String> passwordReset(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");

            User user = userRepository.findByEmail(email);

            if (user == null) {
                return new ResponseEntity<>("Utilisateur non trouvé avec cette email", HttpStatus.BAD_REQUEST);
            }

            String resetToken = passwordService.generateResetToken();

            user.setResetToken(resetToken);
            user.setTokenCreatedAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);

            passwordService.sendResetEmail(email, resetToken);

            return new ResponseEntity<>("E-mail de réinitialisation envoyé avec succès", HttpStatus.OK);
        } catch (Exception e) {
            logger.info("Erreur lors de la création de l'envoie du mail : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        try {
            User user = userRepository.findByEmail(resetPasswordDTO.getEmail());
            if (user == null) {
                return new ResponseEntity<>("Utilisateur non trouvé avec cette email", HttpStatus.BAD_REQUEST);
            }

            if (!resetPasswordDTO.getPassword().equals(resetPasswordDTO.getConfirmPassword())) {
                return new ResponseEntity<>("Les mots de passe ne correspondent pas !", HttpStatus.BAD_REQUEST);
            }

            String tokenValidationMessage = passwordService.validateResetToken(user, resetPasswordDTO.getToken());

            if (!tokenValidationMessage.equals("Token valide")) {
                return new ResponseEntity<>(tokenValidationMessage, HttpStatus.BAD_REQUEST);
            }

            passwordService.resetPassword(user.getEmail(), resetPasswordDTO.getPassword());

            return new ResponseEntity<>("Mot de passe réinitialisé avec succès", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la réinitilisation du mot de passe", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

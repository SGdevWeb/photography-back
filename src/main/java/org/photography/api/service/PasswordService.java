package org.photography.api.service;

import jakarta.persistence.EntityNotFoundException;
import org.photography.api.dto.PasswordDTO.UpdatePasswordDTO;
import org.photography.api.model.User;
import org.photography.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordService {

    private static final int TOKEN_LENGTH = 20;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String generateResetToken() {
        String token = UUID
                .randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, TOKEN_LENGTH);
        return token;
    }

    public String validateResetToken(User user, String token) {
        String userResetToken = user.getResetToken();
        if (!token.equals(userResetToken)) {
            return "Token de réinitialisation invalide";
        }

        Timestamp tokenCreatedAt = user.getTokenCreatedAt();
        if (tokenCreatedAt == null) {
            return "Le token de réinitialisation a expiré";
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = tokenCreatedAt.toLocalDateTime().plusMinutes(60);
        if (now.isAfter(expirationTime)) {
            return "Le token de réinitialisation a expiré";
        }

        return "Token valide";
    }

    public void sendResetEmail(String email, String resetToken) {
        int validityDurationMinutes = 60;

        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(validityDurationMinutes);

        String resetEmailText = String.format("""
                Pour réinitialiser votre mot de passe, veuillez cliquer sur le lien suivant : 
                
                http://localhost:5173/admin/password/reset?token=%s
                
                Ce lien est valide jusqu'au %s. Après cette heure, le lien expirera et vous devrez en demander un nouveau.
                
                """, resetToken, expirationTime);

        emailService.sendEmail(
                email,
                "Réinitialisation de mot de passe",
                resetEmailText,
                "contact@samuelgustin.fr");
    }

    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new EntityNotFoundException("Utilisateur non trouvé.");
        }

        String hashedPassword = passwordEncoder.encode(newPassword);

        user.setPassword(hashedPassword);

        user.setResetToken(null);

        userRepository.save(user);
    }

    public void changePassword(UpdatePasswordDTO updatePasswordDTO) {
        if (!updatePasswordDTO.getNewPassword().equals(updatePasswordDTO.getConfirmNewPassword())) {
            throw new IllegalArgumentException("Les mots de passe ne correspondent pas !");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);

        if (!passwordEncoder.matches(updatePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("L'ancien mot de passe est incorrect");
        }

        resetPassword(user.getEmail(), updatePasswordDTO.getNewPassword());

    }

}

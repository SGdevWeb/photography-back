package org.photography.api.service;

import jakarta.persistence.EntityNotFoundException;
import org.photography.api.model.User;
import org.photography.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

}

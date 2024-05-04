package org.photography.api.controller;

import org.photography.api.dto.LoginDTO;
import org.photography.api.model.User;
import org.photography.api.repository.UserRepository;
import org.photography.api.service.JWTService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final AuthenticationManager authenticationManager;

    private JWTService jwtService;

    private UserRepository userRepository;

    public LoginController(JWTService jwtService,
                           AuthenticationManager authenticationManager,
                           UserRepository userRepository) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<String> getToken(@RequestBody LoginDTO loginDTO) {
        try {
            User user = userRepository.findByUsername(loginDTO.getUsername());

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedPassword = encoder.encode(loginDTO.getPassword());

            System.out.println(hashedPassword);

            if (user == null) {
                return new ResponseEntity<>("Nom d'utilisateur ou mot de passe incorrect.", HttpStatus.UNAUTHORIZED);
            }


            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );

            String token = jwtService.generateToken(authentication);

            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Nom d'utilisateur ou mot de passe incorrect.", HttpStatus.UNAUTHORIZED);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Echec de l'authentification : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR) ;
        }
    }

}

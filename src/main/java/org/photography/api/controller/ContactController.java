package org.photography.api.controller;

import org.photography.api.dto.ContactFormDTO;
import org.photography.api.service.ContactService;
import org.photography.api.service.ThemeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

    @PostMapping("/sendContactMessage")
    public ResponseEntity<?> sendContactMessage(@RequestBody ContactFormDTO form) {
        try{
            contactService.processContactForm(form);
            return new ResponseEntity<>("Message envoyé avec succès", HttpStatus.OK);
        } catch (MailException e) {
            logger.error("Erreur lors de l'envoi du message de contact : ", e);
            return new ResponseEntity<>("Erreur lors de l'envoi du message : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoie du message de contact : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

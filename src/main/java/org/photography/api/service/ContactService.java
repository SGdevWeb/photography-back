package org.photography.api.service;

import org.photography.api.dto.ContactFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    @Autowired
    private EmailService emailService;

    @Value("${contact.email}")
    private String contactEmail;

    public void processContactForm(ContactFormDTO form) throws MailException {
        String message = form.getMessage();

        emailService.sendEmail(contactEmail, form.getSubject(), message, form.getEmail());
    }

}

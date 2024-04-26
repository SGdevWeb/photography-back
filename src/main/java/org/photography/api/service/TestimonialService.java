package org.photography.api.service;

import org.modelmapper.ModelMapper;
import org.photography.api.dto.TestimonialDTO;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.model.Testimonial;
import org.photography.api.repository.TestimonialRepository;
import org.photography.api.utils.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TestimonialService {

    private final TestimonialRepository testimonialRepository;

    private final EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(TestimonialService.class);

    @Autowired
    public TestimonialService(TestimonialRepository testimonialRepository, EmailService emailService) {
        this.testimonialRepository = testimonialRepository;
        this.emailService = emailService;
    }

    @Autowired
    private ModelMapper modelMapper;

    @Value("${contact.email}")
    private String contactEmail;

    public TestimonialDTO createTestimonial(TestimonialDTO testimonialDTO) {
        ValidationUtils.validateName(testimonialDTO.getLastName());
        ValidationUtils.validateName(testimonialDTO.getFirstName());
        ValidationUtils.validateText(testimonialDTO.getTestimonialText());

        Testimonial testimonialToCreate = modelMapper.map(testimonialDTO, Testimonial.class);

        testimonialToCreate.setIsValid(false);
        testimonialToCreate.setIsProcessed(false);

        Testimonial createdTestimonial = testimonialRepository.save(testimonialToCreate);

        sendTestimonialConfirmationEmail(testimonialToCreate);
        sendNotificationToAdmin(testimonialDTO);

        return modelMapper.map(createdTestimonial, TestimonialDTO.class);
    }

    public TestimonialDTO getTestimonialById(Long id) {
        ValidationUtils.validateId(id);

        Testimonial testimonial = testimonialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Testimonial", id));

        return modelMapper.map(testimonial, TestimonialDTO.class);
    }

    public Set<TestimonialDTO> getAllTestimonials() {
        Set<Testimonial> testimonials = new HashSet<>(testimonialRepository.findAll());

        return testimonials.stream()
                .map(testimonial -> modelMapper.map(testimonial, TestimonialDTO.class))
                .collect(Collectors.toSet());
    }

    public TestimonialDTO updateTestimonial(Long id, TestimonialDTO updatedTestimonialDTO) {
        ValidationUtils.validateId(id);
        ValidationUtils.validateName(updatedTestimonialDTO.getLastName());
        ValidationUtils.validateName(updatedTestimonialDTO.getFirstName());
        ValidationUtils.validateText(updatedTestimonialDTO.getTestimonialText());

        Testimonial existingTestimonial = testimonialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Testimonial", id));

        existingTestimonial.setLastName(updatedTestimonialDTO.getLastName());
        existingTestimonial.setFirstName(updatedTestimonialDTO.getFirstName());
        existingTestimonial.setTestimonialText(updatedTestimonialDTO.getTestimonialText());

        Testimonial updatedTestimonial = testimonialRepository.save(existingTestimonial);

        return modelMapper.map(updatedTestimonial, TestimonialDTO.class);
    }

    public void deleteTestimonial(Long id) {
        ValidationUtils.validateId(id);

        if (testimonialRepository.existsById(id)) {
            testimonialRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Testimonial", id);
        }
    }

    public void approveTestimonial(long testimonialId) {
        Testimonial existingTestimonial = testimonialRepository.findById(testimonialId)
                .orElseThrow(() -> new EntityNotFoundException("Testimonial", testimonialId));

        existingTestimonial.setIsValid(true);
        existingTestimonial.setIsProcessed(true);

        sendTestimonialApprovalEmail(existingTestimonial.getId());

        testimonialRepository.save(existingTestimonial);
    }

    public void rejectTestimonial(long testimonialId) {
        Testimonial existingTestimonial = testimonialRepository.findById(testimonialId)
                .orElseThrow(() -> new EntityNotFoundException("Testimonial", testimonialId));

        existingTestimonial.setIsValid(false);
        existingTestimonial.setIsProcessed(true);

        sendTestimonialRejectEmail(existingTestimonial.getId());

        testimonialRepository.save(existingTestimonial);
    }

    public void sendTestimonialConfirmationEmail(Testimonial testimonialObject) {

        String fullName = testimonialObject.getFirstName() + " " + testimonialObject.getLastName();
        String clientEmail = testimonialObject.getEmail();

        String emailContent = String.format("""
        Cher(e) %s,

        Nous vous remercions d'avoir pris le temps de laisser votre avis sur notre site. Votre opinion est précieuse pour nous et nous sommes impatients de la lire.

        Nous tenons à vous informer que votre avis est en cours de traitement. Nous accordons une grande importance à la qualité et à la pertinence des avis que nous publions sur notre plateforme. Par conséquent, chaque avis est soumis à une vérification avant d'être publié.

        Nous nous efforçons de garantir que tous les avis publiés sont authentiques, respectent nos conditions d'utilisation et ne contiennent aucun contenu inapproprié tel que des propos injurieux, racistes ou diffamatoires.

        Nous vous remercions pour votre compréhension et votre patience pendant ce processus. Nous vous informerons dès que votre avis sera approuvé et publié sur notre plateforme.

        Merci encore pour votre contribution précieuse.

        Cordialement,""", fullName);

        emailService.sendEmail(
                clientEmail,
                "Confirmation de réception de votre témoignage",
                emailContent,
                contactEmail
        );
    }

    public void sendTestimonialApprovalEmail(long testimonialId) {
        Testimonial existingTestimonial = testimonialRepository.findById(testimonialId)
                .orElseThrow(() -> new EntityNotFoundException("Testimonial", testimonialId));

        String fullName = existingTestimonial.getFirstName() + " " + existingTestimonial.getLastName();
        String clientEmail = existingTestimonial.getEmail();

        String emailContent = String.format("""
        Cher(e) %s,

        Nous avons le plaisir de vous informer que votre témoignage a été approuvé et publié sur notre plateforme. Nous tenons à vous remercier pour votre contribution précieuse.
                                                               
        Nous espérons que votre expérience avec nous a été positive et que nous aurons le plaisir de vous servir à nouveau à l'avenir.

        Cordialement,""", fullName);

        emailService.sendEmail(
                clientEmail,
                "Confirmation de publication de votre témoignage",
                emailContent,
                contactEmail
        );

    }

    public void sendTestimonialRejectEmail(long testimonialId) {
        Testimonial existingTestimonial = testimonialRepository.findById(testimonialId)
                .orElseThrow(() -> new EntityNotFoundException("Testimonial", testimonialId));

        String fullName = existingTestimonial.getFirstName() + " " + existingTestimonial.getLastName();
        String clientEmail = existingTestimonial.getEmail();

        String emailContent = String.format("""
        Cher(e) %s,

        Nous regrettons de vous informer que votre témoignage n'a pas été approuvé pour publication sur notre plateforme. Après avoir examiné votre avis, nous avons déterminé qu'il ne répond pas à nos critères de publication en raison de son contenu inapproprié.
                        
        Nous vous encourageons à soumettre à nouveau votre témoignage sans contenus inappropriés tel que des propos injurieux, racistes ou diffamatoires.
                        
        Nous vous remercions de votre compréhension.

        Cordialement,""", fullName);

        emailService.sendEmail(
                clientEmail,
                "Confirmation de rejet de votre témoignage",
                emailContent,
                contactEmail
        );

    }

    public void sendNotificationToAdmin(TestimonialDTO testimonialDTO) {
        String emailContent = String.format("""
                Bonjour,
                                
                Un nouveau témoignage a été soumis sur la plateforme.
                
                Détails du témoignage :
                Prénom : %s
                Nom : %s
                E-mail : %s
                
                Merci de vérifier et de traiter ce témoignage sur votre dashboard.
                """, testimonialDTO.getFirstName(), testimonialDTO.getLastName(), testimonialDTO.getEmail());

        emailService.sendEmail(
                contactEmail,
                "Nouveau témoignage soumis sur la plateforme",
                emailContent,
                contactEmail
        );
    }

}

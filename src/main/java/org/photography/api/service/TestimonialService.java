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
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TestimonialService {

    private final TestimonialRepository testimonialRepository;
    private static final Logger logger = LoggerFactory.getLogger(TestimonialService.class);

    @Autowired
    public TestimonialService(TestimonialRepository testimonialRepository) {
        this.testimonialRepository = testimonialRepository;
    }

    @Autowired
    private ModelMapper modelMapper;

    public TestimonialDTO createTestimonial(TestimonialDTO testimonialDTO) {
        ValidationUtils.validateName(testimonialDTO.getLastName());
        ValidationUtils.validateName(testimonialDTO.getFirstName());
        ValidationUtils.validateText(testimonialDTO.getTestimonialText());

        testimonialDTO.setIsValid(false);

        Testimonial testimonialToCreate = modelMapper.map(testimonialDTO, Testimonial.class);

        Testimonial createdTestimonial = testimonialRepository.save(testimonialToCreate);

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

}

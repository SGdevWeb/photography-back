package org.photography.api.controller;

import org.photography.api.dto.TestimonialDTO;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.service.TestimonialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testimonials")
public class TestimonialController {

    private final TestimonialService testimonialService;

    @Autowired
    public TestimonialController(TestimonialService testimonialService) {
        this.testimonialService = testimonialService;
    }

    @PostMapping
    public ResponseEntity<?> createTestimonial(@RequestBody TestimonialDTO testimonialDTO) {
        try {
            TestimonialDTO createdTestimonial = testimonialService.createTestimonial(testimonialDTO);
            return new ResponseEntity<>(createdTestimonial, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{testimonialId}")
    public ResponseEntity<?> getTestimonialById(@PathVariable Long testimonialId) {
        try {
            TestimonialDTO testimonial = testimonialService.getTestimonialById(testimonialId);
            return new ResponseEntity<>(testimonial, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllTestimonials() {
        try {
            List<TestimonialDTO> testimonials = testimonialService.getAllTestimonials();
            return new ResponseEntity<>(testimonials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{testimonialId}")
    public ResponseEntity<?> updateTestimonial(@PathVariable Long testimonialId, @RequestBody TestimonialDTO updatedTestimonialDTO) {
        try {
            TestimonialDTO updatedTestimonial = testimonialService.updateTestimonial(testimonialId, updatedTestimonialDTO);
            return new ResponseEntity<>(updatedTestimonial, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{testimonialId}")
    public ResponseEntity<?> deleteTestimonial(@PathVariable Long testimonialId) {
        try {
            testimonialService.deleteTestimonial(testimonialId);
            return new ResponseEntity<>("Testimonial successfully deleted", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

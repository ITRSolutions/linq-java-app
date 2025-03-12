package com.linq.website.service;

import com.linq.website.dto.FormSubmissionDTO;
import com.linq.website.entity.FormSubmission;
import com.linq.website.exceptions.ResourceNotFoundException;
import com.linq.website.repository.FormSubmissionRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Validated
@Service
public class FormSubmissionService {

    @Autowired
    private FormSubmissionRepository repository;

    // Create a logger instance for your class
    private static final Logger logger = Logger.getLogger(FormSubmissionService.class.getName());

    public FormSubmission saveSubmission(FormSubmissionDTO dto) {
        FormSubmission formSubmission = new FormSubmission();
        formSubmission.setFirstName(dto.getFirstName());
        formSubmission.setLastName(dto.getLastName());
        formSubmission.setDateOfBirth(dto.getDateOfBirth());
        formSubmission.setPhoneNumber(dto.getPhoneNumber());
        formSubmission.setEmail(dto.getEmail());
        formSubmission.setBestContactTime(dto.getBestContactTime());
        formSubmission.setZipCode(dto.getZipCode());
        formSubmission.setState(dto.getState());
        formSubmission.setSpeciality(dto.getSpeciality());
        formSubmission.setIsEmployee(dto.getIsEmployee());
        formSubmission.setIsStudyParticipant(dto.getIsStudyParticipant());
        formSubmission.setAgreedToTerms(dto.getAgreedToTerms());

        return repository.save(formSubmission);
    }

    public List<FormSubmission> getAllFormSubmissions() {
        return repository.findAll();
    }

    public FormSubmission getFormById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FormSubmission not found with id: " + id));
    }

    public List<FormSubmission> getAllSubmissions() {
        return repository.findAll();
    }

    public List<FormSubmission> getFormDataByPageName(String pageName) {
        Optional<List<FormSubmission>> byPageName = Optional.ofNullable(repository.getByPageName(pageName));
        return byPageName.orElseThrow(() -> {
            logger.log(Level.parse("FormSubmission not found with page name: {}"), pageName);
            return new ResourceNotFoundException("FormSubmission not found with page name: " + pageName);
        });
    }

    public List<FormSubmission> getFormDataByPageId(Long pageId) {
        Optional<List<FormSubmission>> byPageId = Optional.ofNullable(repository.getByPageId(pageId));

         return byPageId.orElseThrow(()->{
             logger.log(Level.parse("FormSubmission not found with page id: {}"), String.valueOf(pageId));
             return new ResourceNotFoundException("FormSubmission not found with page id: " + pageId);
         });
    }
}

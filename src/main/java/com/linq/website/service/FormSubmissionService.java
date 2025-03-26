package com.linq.website.service;

import com.linq.website.dto.FormSubmissionDTO;
import com.linq.website.entity.FormSubmission;
import com.linq.website.exceptions.ResourceNotFoundException;
import com.linq.website.repository.FormSubmissionRepository;
import com.linq.website.utility.LoggedUser;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    LoggedUser loggedUser;

    // Create a logger instance for your class
    private static final Logger logger = Logger.getLogger(FormSubmissionService.class.getName());

    public void saveSubmission(FormSubmissionDTO.Create dto) {
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
        formSubmission.setPageId(dto.getPageId());
        formSubmission.setPageName(dto.getPageName());
        formSubmission.setRefName(dto.getRefName());
        formSubmission.setRefContactNumber(dto.getRefContactNumber());

        repository.save(formSubmission);
    }

    public void updateSubmission(FormSubmissionDTO.Update dto, Long id) {
        FormSubmission formSubmission = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("DynamicPage not found"));

        formSubmission.setFollowBack(dto.getFollowBack());
        formSubmission.setComment(dto.getComment());
        formSubmission.setUpdatedBy(loggedUser.getUpdatedByUserObj());

        repository.save(formSubmission);
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

    public Page<FormSubmission> getFormDataByPageNamePagination(String pageName, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        return repository.findByPageName(pageName, pageable);
    }

    public List<FormSubmission> getFormDataByPageId(Long pageId) {
        Optional<List<FormSubmission>> byPageId = Optional.ofNullable(repository.getByPageId(pageId));

         return byPageId.orElseThrow(()->{
             logger.log(Level.parse("FormSubmission not found with page id: {}"), String.valueOf(pageId));
             return new ResourceNotFoundException("FormSubmission not found with page id: " + pageId);
         });
    }

    public List<FormSubmission> searchSubmissionsByStringAndPageName(String searchString, String pageName) {
        return repository.searchByStringAndPageName(searchString, pageName);
    }
}

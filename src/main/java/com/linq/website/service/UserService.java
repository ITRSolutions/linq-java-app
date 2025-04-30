package com.linq.website.service;

import com.linq.website.dto.ContactUsDTO;
import com.linq.website.dto.UserDTO;
import com.linq.website.entity.User;
import com.linq.website.enums.RoleType;
import com.linq.website.repository.UserRepository;
import com.linq.website.utility.LoggedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AsyncMailExecutor asyncMailExecutor;

    @Autowired
    LoggedUser loggedUser;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class); // Use logger

    // Count total users by role
    public Long countUsers(RoleType role) {
        return userRepository.countByRole(role);
    }

    // Find all resources by role with paginated
    public Page<User> getUsersListPagination(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));

        // Fetch paginated results from the repository
        return userRepository.findAll(pageable);
    }

    // Find user by id
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Find user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    // Find user by contact number
    public Optional<User> findByContactNumber(String contactNumber) {
        return userRepository.findByContactNumber(contactNumber);
    }

    public Optional<User> findByEmailAndOtpCode(String email, String otpCode) {
        return userRepository.findByEmailAndPasswordResetOtp(email, otpCode);
    }

    public Optional<User> findByPasswordResetRef(String ref) {
        return userRepository.findByPasswordResetRef(ref);
    }

    // Create a new user
    public void saveUser(UserDTO.CreateUser data, String hashPassword, RoleType role) {
        User userData = new User();
        userData.setFirstName(data.getFirstName());
        userData.setLastName(data.getLastName());
        userData.setContactNumber(data.getContactNumber());
//        userData.setCity(data.getCity());
//        userData.setState(data.getState());
//        userData.setCountry(data.getCountry());
//        userData.setZipCode(data.getZipCode());
        userData.setEmail(data.getEmail());
        userData.setPassword(hashPassword);
//        userData.setDob(data.getDob());
//        userData.setGender(data.getGender());
        userData.setRole(role);
        userData.setActivateUser(data.getActivateUser());
        userRepository.save(userData);

        asyncMailExecutor.sendActivationEmail(userData);

        //Sending email to admins -> New person register
        sendEmailsAdmin(2, userData);
    }

    // Update a specific user
    public Boolean updateUser(Long id, UserDTO.UpdateUser data) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            return false;
        }

        User userData = existingUser.get();
        userData.setFirstName(data.getFirstName());
        userData.setLastName(data.getLastName());
        userData.setContactNumber(data.getContactNumber());
        userData.setCity(data.getCity());
        userData.setState(data.getState());
        userData.setCountry(data.getCountry());
        userData.setZipCode(data.getZipCode());
        userData.setGender(data.getGender());
        userData.setActivateUser(data.getActivateUser());
        userData.setDob(data.getDob());

        User updatedByUserObj = loggedUser.getUpdatedByUserObj();
        userData.setUpdatedBy(updatedByUserObj.getFirstName()+" "+updatedByUserObj.getLastName());

        userRepository.save(userData);
        return true;
    }

    // Update specific resource password
    public void updatePassword(Long id, String hashPassword) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User userData = existingUser.get();
            userData.setPassword(hashPassword);
            userRepository.save(userData);
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void updatePasswordResetOtp(Long id, String token) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User userData = existingUser.get();
            userData.setPasswordResetOtp(token);
            userRepository.save(userData);
        }
    }

    // Update specific resource password reset Ref
    public void updatePasswordResetRef(Long id, String ref) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User userData = existingUser.get();
            userData.setPasswordResetRef(ref);
            userRepository.save(userData);
        }
    }

    public Optional<User> activateRegistration(String key) {
        Optional<User> user = userRepository.findByEmailIgnoreCase(key);
        if (user.isPresent()) {
            user.get().setIsEmailVerified(true);
            userRepository.save(user.get());
        } else {
            // Log the error
            logger.error("Failed to activate user email, because admin not found: " + key);
        }

        return user;
    }

    public String sendContactUsEnquiryMail(ContactUsDTO contactDTO) {
        logger.info("END: sendContactUsEnquiryMail()");
        return sendEmailsAdmin(1, contactDTO);
    }

    public String sendEmailsAdmin(int stat, Object obj) {
        List<User> admins = userRepository.findByRole(RoleType.ADMIN);

        if (admins.isEmpty()) {
            return false + "admins.isEmpty(): "+admins.isEmpty(); // No admins found
        }

        String temp = null;
        for (User admin : admins) {
            try {
                switch (stat) {
                    case 1:
                        logger.info("Sending mails calling method: sendContactUsEnquiryMail");
                        return temp = asyncMailExecutor.sendContactUsEnquiryMail((ContactUsDTO) obj, admin);

                    case 2:
                        asyncMailExecutor.sendNewUserRegisterEmail((User) obj, admin);
                        break;
                }
            } catch (Exception e) {
                logger.error("Failed to send email to admin: " + admin.getEmail(), e);
            }
        }
        return temp;
    }

    // Search for users by a search term (could be firstName, lastName, or email)
    public List<User> searchUsers(String searchTerm) {
        return userRepository.searchUsers(searchTerm);
    }

    public long getTotalUserRegistered() {
        return userRepository.count();
    }

    public long getTotalUnverifiedUser() {
        return userRepository.countUnverifiedUsers();
    }

}

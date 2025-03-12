package com.linq.website.service;

import com.linq.website.dto.ContactUsDTO;
import com.linq.website.dto.UserDTO;
import com.linq.website.entity.User;
import com.linq.website.enums.RoleType;
import com.linq.website.repository.UserRepository;
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
    private MailService mailService;

    // Count total users by role
    public Long countUsers(RoleType role) {
        return userRepository.countByRole(role);
    }

    // Find all resources by role with paginated
    public Page<User> getUsersByRoleWithPagination(RoleType role, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));

        // Fetch paginated results from the repository
        return userRepository.findAllByRole(role, pageable);
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
        userData.setCity(data.getCity());
        userData.setState(data.getState());
        userData.setCountry(data.getCountry());
        userData.setZipCode(data.getZipCode());
        userData.setEmail(data.getEmail());
        userData.setPassword(hashPassword);
        userData.setDob(data.getDob());
        userData.setGender(data.getGender());
        userData.setRole(role);
        userRepository.save(userData);
        mailService.sendActivationEmail(userData);
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
        }
        return user;
    }

    public void sendContactUsEnquiryMail(ContactUsDTO contactDTO) {
        List<User> admins = userRepository.findByRole(RoleType.ADMIN);

        // Handle case when no admin is found
        if (admins.isEmpty()) {
            return ;  // No admins found
        }

        int successfulSends = 0;
        for (User user : admins) {
            try {
                mailService.sendContactUsEnquiryMail(contactDTO, user);
                successfulSends++;
            } catch (Exception e) {
                // Log the error, you could use a logger here
                LoggerFactory.getLogger(getClass()).error("Failed to send email to admin: " + user.getEmail(), e);
            }
        }
    }

}

package com.linq.website.repository;


import com.linq.website.entity.DynamicPage;
import com.linq.website.entity.User;
import com.linq.website.enums.PageStatus;
import com.linq.website.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Count resources by their role
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    Long countByRole(@Param("role") RoleType role);

    // Retrieve list of resources by role as paginated
    Page<User> findAll(Pageable pageable);

    // Custom query method to find a user by email
    Optional<User> findByEmailIgnoreCase(String email);

    // Custom query method to find a user by contact number
    Optional<User> findByContactNumber(String contactNumber);

    // Custom query method to find a user by email and OTP code
    Optional<User> findByEmailAndPasswordResetOtp(String email, String passwordResetOtp);

    // Custom query method to find a user by password reset ref
    Optional<User> findByPasswordResetRef(String passwordResetRef);

    List<User> findByRole(RoleType role);

    // Search users by first name, last name, or email
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> searchUsers(String searchTerm);

    long count();

    // Method to count the total number of users where email is not verified (isEmailVerified = false)
    @Query("SELECT COUNT(u) FROM User u WHERE u.isEmailVerified = false")
    long countUnverifiedUsers();

}

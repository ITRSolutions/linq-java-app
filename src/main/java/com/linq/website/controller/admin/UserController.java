package com.linq.website.controller.admin;

import com.linq.website.dto.SuccessResponse;
import com.linq.website.dto.UserDTO;
import com.linq.website.entity.User;
import com.linq.website.service.UserService;
import com.linq.website.utility.Helpers;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@PreAuthorize("hasRole('ADMIN')") // Ensures only ADMIN can access
public class UserController {

    @Autowired
    UserService userService;

    // Get all users
    @GetMapping()
    public ResponseEntity<?> getAllDynamicPage(@RequestParam(defaultValue = "0") int page) {
        Page<User> users = userService.getUsersListPagination(page);
        Helpers.PaginationResponsePattern<User> response = new Helpers.PaginationResponsePattern<>(users.getTotalElements(), users.getContent());

        return ResponseEntity.ok(new SuccessResponse<>(true, "List of Users.", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteSlide(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new SuccessResponse<>(true, "User is deleted successfully id: "+id, null));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam String searchTerm) {
        List<User> users = userService.searchUsers(searchTerm);
        return ResponseEntity.ok(new SuccessResponse<>(true, "Search result for users.", users));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO.UpdateUser dto) {
        userService.updateUser(id, dto);
        return ResponseEntity.ok(new SuccessResponse<>(true, "User data update successfully", null));
    }
}

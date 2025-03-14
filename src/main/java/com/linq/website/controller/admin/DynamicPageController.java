package com.linq.website.controller.admin;

import com.linq.website.dto.DynamicPageDTO;
import com.linq.website.dto.ErrorResponse;
import com.linq.website.dto.SuccessResponse;
import com.linq.website.entity.DynamicPage;
import com.linq.website.exceptions.PageNotFoundException;
import com.linq.website.service.DynamicPageService;
import com.linq.website.utility.Helpers;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/web_page")
public class DynamicPageController {

    @Autowired
    DynamicPageService dynamicPageService;

    @PostMapping("")
    public ResponseEntity<?> createDynamicPage(@Valid @RequestBody DynamicPageDTO.CreateDynamicPage dto) {
        try {
            DynamicPage pageBySlug = dynamicPageService.getPageBySlug(dto.getSlug());

            Map<String, String[]> errors = new HashMap<>();
            errors.put("Web page exist", new String[]{"The web page name is already exist."});
            ErrorResponse<Map<String, String[]>> errorResponse = new ErrorResponse<>("Found existing data.", errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (PageNotFoundException e) {
            dynamicPageService.saveDynamicPage(dto);
        }

        return ResponseEntity.ok(new SuccessResponse<>(true, "Web-page created successfully.", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDynamicPage(@Valid @RequestBody DynamicPageDTO.UpdateDynamicPage dto, @PathVariable Long id) {
        try {
            dynamicPageService.getPageBySlug(dto.getSlug());
            dynamicPageService.updateDynamicPage(dto, id);
            System.out.println("updateDynamicPage");
        } catch (PageNotFoundException e) {
            Map<String, String[]> errors = new HashMap<>();
            errors.put("Web page exist", new String[]{"The web page name not exist."});
            ErrorResponse<Map<String, String[]>> errorResponse = new ErrorResponse<>("Mismatch data.", errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(new SuccessResponse<>(true, "Web-page updated successfully.", null));
    }

    @GetMapping()
    public ResponseEntity<?> getAllDynamicPage(@RequestParam(defaultValue = "0") int page) {
        Page<DynamicPage> users = dynamicPageService.getDynamicPagesWithPagination(page);
        Helpers.PaginationResponsePattern<DynamicPage> response = new Helpers.PaginationResponsePattern<>(users.getTotalElements(), users.getContent());

        return ResponseEntity.ok(new SuccessResponse<>(true, "List of Web-pages.", response));
    }
}

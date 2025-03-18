package com.linq.website.controller.admin;

import com.linq.website.dto.DynamicPageDTO;
import com.linq.website.dto.ErrorResponse;
import com.linq.website.dto.SuccessResponse;
import com.linq.website.entity.DynamicPage;
import com.linq.website.entity.User;
import com.linq.website.exceptions.PageNotFoundException;
import com.linq.website.service.DynamicPageService;
import com.linq.website.utility.Helpers;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/web_page")
public class DynamicPageController {

    @Autowired
    DynamicPageService dynamicPageService;

    // The folder where the .html files are located
    private static final String HTML_FOLDER_PATH = "src/main/resources/templates/public";
    private static final String HTML_FOLDER_PATH1 = "src/main/resources/templates/fragments";

    @PostMapping("")
    public ResponseEntity<?> createDynamicPage(@Valid @RequestBody DynamicPageDTO.CreateDynamicPage dto) {
        try {
            DynamicPage pageBySlug = dynamicPageService.getPageByName(dto.getSlug());

            ErrorResponse<Map<String, String[]>> errorResponse = new ErrorResponse<>("The web page name is already exist.", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (PageNotFoundException e) {
            dynamicPageService.saveDynamicPage(dto);

        }

        return ResponseEntity.ok(new SuccessResponse<>(true, "Web-page created successfully.", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDynamicPage(@Valid @RequestBody DynamicPageDTO.UpdateDynamicPage dto, @PathVariable Long id) {
        try {
            dynamicPageService.getPageByName(dto.getSlug());
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

    @PostMapping("/search")
    public ResponseEntity<?> searchDynamicPages(@Valid @RequestBody DynamicPageDTO.SearchDynamicPage searchDTO) {
        List<DynamicPage> dynamicPages = dynamicPageService.searchDynamicPages(searchDTO);
        return ResponseEntity.ok(new SuccessResponse<>(true, "Search result for web page.", dynamicPages));
    }

    @GetMapping("/getallnames")
    public ResponseEntity<?> listHtmlFiles() throws IOException {
        // Get all .html files in the folder
        List<String> htmlFiles = listHtmlFilesInFolder(HTML_FOLDER_PATH);
        List<String> htmlFiles1 = listHtmlFilesInFolder(HTML_FOLDER_PATH1);

        List<String> allHtmlFile = Stream.concat(htmlFiles.stream(), htmlFiles1.stream()).toList();

        return ResponseEntity.ok(new SuccessResponse<>(true, "Web-pages list.", allHtmlFile));
    }

    private List<String> listHtmlFilesInFolder(String folderPath) throws IOException {
        // Create a Path object for the folder
        Path path = Paths.get(folderPath);

        // List all files and filter for .html files
        return Files.walk(path)
                .filter(Files::isRegularFile)  // Only regular files (not directories)
                .map(Path::toString)
                .filter(f -> f.endsWith(".html"))  // Filter for .html files
                .map(f -> new File(f).getName().replace(".html",""))  // Get just the file name, not the full path
                .collect(Collectors.toList());
    }
}

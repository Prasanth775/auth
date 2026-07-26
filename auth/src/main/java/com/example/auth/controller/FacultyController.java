package com.example.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.auth.service.FacultyService;
import com.example.auth.model.Faculty;
import com.example.auth.util.FileUploadUtil;

import java.util.List;

@RestController
@RequestMapping("/faculty")
@CrossOrigin(origins = "http://localhost:3000")
public class FacultyController {

    @Autowired
    private FacultyService service;

    // ➕ ADD FACULTY
    @PostMapping("/add")
    public ResponseEntity<?> add(
            @RequestParam String name,
            @RequestParam String roomNo,
            @RequestParam String department,
            @RequestParam String block,
            @RequestParam String mobile,
            @RequestParam Long userId,
            @RequestParam MultipartFile photo
    ) {
        try {
            Faculty f = new Faculty();
            f.setName(name);
            f.setRoomNo(roomNo);
            f.setDepartment(department);
            f.setBlock(block);
            f.setMobile(mobile);
            f.setUserId(userId);

            if (photo != null && !photo.isEmpty()) {
                f.setPhoto(FileUploadUtil.saveFile(photo));
            }

            return ResponseEntity.ok(service.add(f));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error: " + e.getMessage());
        }
    }

    // 👤 GET FACULTY BY USER ID
    @GetMapping("/{userId}")
    public ResponseEntity<List<Faculty>> get(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getAll(userId));
    }

    // 🔍 GET ALL FACULTIES
    @GetMapping("/all")
    public ResponseEntity<List<Faculty>> getAllFaculties() {
        return ResponseEntity.ok(service.getAllFaculties());
    }

    // ❌ DELETE FACULTY
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted Successfully");
    }

    // ✏️ UPDATE WITHOUT IMAGE
    @PutMapping("/{id}")
    public ResponseEntity<Faculty> update(
            @PathVariable Long id,
            @RequestBody Faculty f
    ) {
        return ResponseEntity.ok(service.update(id, f));
    }

    // ✏️ UPDATE WITH IMAGE (FIXED & SAFE)
    @PutMapping("/{id}/upload")
    public ResponseEntity<?> updateWithImage(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String roomNo,
            @RequestParam String department,
            @RequestParam String block,
            @RequestParam String mobile,
            @RequestParam Long userId,
            @RequestParam(required = false) MultipartFile photo
    ) {
        try {
            // 🔥 IMPORTANT FIX: LOAD EXISTING DATA FIRST
            Faculty existing = service.getAllFaculties()
                    .stream()
                    .filter(f -> f.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Faculty not found"));

            // update fields
            existing.setName(name);
            existing.setRoomNo(roomNo);
            existing.setDepartment(department);
            existing.setBlock(block);
            existing.setMobile(mobile);
            existing.setUserId(userId);

            // update photo only if provided
            if (photo != null && !photo.isEmpty()) {
                existing.setPhoto(FileUploadUtil.saveFile(photo));
            }

            return ResponseEntity.ok(service.add(existing)); // save updated entity

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error: " + e.getMessage());
        }
    }
}
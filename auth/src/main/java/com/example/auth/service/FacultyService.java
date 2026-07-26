package com.example.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.auth.repository.FacultyRepository;
import com.example.auth.model.Faculty;

import java.util.List;

@Service
public class FacultyService {

    @Autowired
    private FacultyRepository repo;

    // ➕ Add Faculty
    public Faculty add(Faculty f) {
        return repo.save(f);
    }

    // 👤 Get Faculty by User
    public List<Faculty> getAll(Long userId) {
        return repo.findByUserId(userId);
    }

    // 🔍 Get ALL Faculties
    public List<Faculty> getAllFaculties() {
        return repo.findAll();
    }

    // ❌ Delete Faculty
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✏️ FINAL UPDATE FIX (WORKING)
    public Faculty update(Long id, Faculty f) {

        Faculty existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        if (f.getName() != null && !f.getName().trim().isEmpty())
            existing.setName(f.getName());

        if (f.getRoomNo() != null && !f.getRoomNo().trim().isEmpty())
            existing.setRoomNo(f.getRoomNo());

        if (f.getDepartment() != null && !f.getDepartment().trim().isEmpty())
            existing.setDepartment(f.getDepartment());

        if (f.getBlock() != null && !f.getBlock().trim().isEmpty())
            existing.setBlock(f.getBlock());

        if (f.getMobile() != null && !f.getMobile().trim().isEmpty())
            existing.setMobile(f.getMobile());

        // ⚠️ PHOTO UPDATE FIX (IMPORTANT)
        if (f.getPhoto() != null && !f.getPhoto().trim().isEmpty()) {
            existing.setPhoto(f.getPhoto());
        }

        return repo.save(existing);
    }
}
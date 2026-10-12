package com.anurag.dvp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anurag.dvp.entity.VisitRequest;
import com.anurag.dvp.repository.VisitRequestRepository;
import com.anurag.dvp.service.VisitService;

@RestController
@RequestMapping("/api/visits")
@CrossOrigin(origins = "*")
public class VisitController {

    private final VisitRequestRepository visitRepo;
    private final VisitService visitService;

    public VisitController(VisitRequestRepository visitRepo, VisitService visitService) {
        this.visitRepo = visitRepo;
        this.visitService = visitService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createVisit(@RequestBody VisitRequest request) {
        try {
            if (request.getStatus() == null) {
                request.setStatus("PENDING");
            }
            VisitRequest saved = visitRepo.save(request);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) throws Exception {
        VisitRequest updated = visitService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/all")
    public ResponseEntity<List<VisitRequest>> getAllVisits() {
        return ResponseEntity.ok(visitRepo.findAll());
    }
}
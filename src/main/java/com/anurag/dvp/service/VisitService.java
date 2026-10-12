package com.anurag.dvp.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.anurag.dvp.entity.VisitRequest;
import com.anurag.dvp.repository.VisitRequestRepository;
import com.anurag.dvp.util.QRCodeUtil;

@Service
public class VisitService {

    private final VisitRequestRepository visitRepo;

    // Constructor Injection (Spring Boot recommended way)
    public VisitService(VisitRequestRepository visitRepo) {
        this.visitRepo = visitRepo;
    }

    public VisitRequest updateStatus(Long requestId, String status) throws Exception {
        VisitRequest request = visitRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(status);
        if ("APPROVED".equalsIgnoreCase(status)) {
            String visitorName = (request.getVisitor() != null && request.getVisitor().getName() != null) 
                    ? request.getVisitor().getName() 
                    : "Guest";
            String qrData = "PASS_ID:" + request.getId() + "_VISITOR:" + visitorName;
            request.setPassQrCode(QRCodeUtil.generateQRCodeBase64(qrData));
        }
        return visitRepo.save(request);
    }

    public VisitRequest checkIn(Long requestId) {
        VisitRequest request = visitRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setCheckInTime(LocalDateTime.now());
        return visitRepo.save(request);
    }

    public VisitRequest checkOut(Long requestId) {
        VisitRequest request = visitRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setCheckOutTime(LocalDateTime.now());
        request.setStatus("COMPLETED");
        return visitRepo.save(request);
    }
}
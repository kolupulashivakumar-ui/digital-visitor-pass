package com.anurag.dvp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anurag.dvp.entity.VisitRequest;

public interface VisitRequestRepository extends JpaRepository<VisitRequest, Long> {
    List<VisitRequest> findByVisitorId(Long visitorId);
    List<VisitRequest> findByHostId(Long hostId);
}
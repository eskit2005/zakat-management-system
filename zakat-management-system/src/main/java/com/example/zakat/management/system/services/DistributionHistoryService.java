package com.example.zakat.management.system.services;

import com.example.zakat.management.system.dtos.response.DistributionHistoryResponse;
import com.example.zakat.management.system.mappers.DistributionHistoryMapper;
import com.example.zakat.management.system.repositories.DistributionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DistributionHistoryService {

    private final DistributionHistoryRepository distributionHistoryRepository;
    private final DistributionHistoryMapper distributionHistoryMapper;

    // DistributionHistoryMapper accesses history.getAssignment().getId() (LAZY OneToOne)
    @Transactional(readOnly = true)
    public List<DistributionHistoryResponse> getAll() {
        return distributionHistoryRepository.findAllByOrderByDistributionDateDesc().stream()
                .map(distributionHistoryMapper::toResponse)
                .toList();
    }
}

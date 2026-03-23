package com.example.zakat.management.system.services;

import com.example.zakat.management.system.dtos.request.BeneficiaryRegisterRequest;
import com.example.zakat.management.system.dtos.response.BeneficiaryResponse;
import com.example.zakat.management.system.entities.Beneficiary;
import com.example.zakat.management.system.entities.User;
import com.example.zakat.management.system.exceptions.DuplicateResourceException;
import com.example.zakat.management.system.exceptions.ResourceNotFoundException;
import com.example.zakat.management.system.mappers.BeneficiaryMapper;
import com.example.zakat.management.system.repositories.BeneficiaryRepository;
import com.example.zakat.management.system.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final UserRepository userRepository;
    private final BeneficiaryMapper beneficiaryMapper;

    @Transactional
    public BeneficiaryResponse register(BeneficiaryRegisterRequest request) {
        if (beneficiaryRepository.findById(request.getUserId()).isPresent()) {
            throw new DuplicateResourceException("A beneficiary profile already exists for this account");
        }


        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setUser(user);
        beneficiary.setFullName(request.getFullName());
        beneficiary.setIsEmergency(request.getIsEmergency() != null && request.getIsEmergency());
        beneficiary.setPriorityScore(0);

        return beneficiaryMapper.toResponse(beneficiaryRepository.save(beneficiary));
    }

    // @Transactional(readOnly = true) keeps the session open so BeneficiaryMapper can
    // access lazy fields: user.id, eligibilityCheck, zakatAssignment
    @Transactional()
    public List<BeneficiaryResponse> getAllBeneficiaries() {
        return beneficiaryRepository.findAll().stream()
                .map(beneficiaryMapper::toResponse)
                .toList();
    }

    @Transactional()
    public BeneficiaryResponse getById(Long id) {
        return beneficiaryMapper.toResponse(
                beneficiaryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with id: " + id))
        );
    }

    @Transactional()
    public List<BeneficiaryResponse> getEligibleQueue() {
        return beneficiaryRepository.findEligibleQueue().stream()
                .map(beneficiaryMapper::toResponse)
                .toList();
    }
}

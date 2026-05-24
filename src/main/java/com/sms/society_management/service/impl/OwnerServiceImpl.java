package com.sms.society_management.service.impl;

import com.sms.society_management.dto.request.OwnerRequest;
import com.sms.society_management.dto.response.OwnerResponse;
import com.sms.society_management.entity.Owner;
import com.sms.society_management.exception.ResourceNotFoundException;
import com.sms.society_management.mapper.OwnerMapper;
import com.sms.society_management.repository.OwnerRepository;
import com.sms.society_management.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    @Override
    public OwnerResponse createOwner(OwnerRequest request) {
        Owner owner = OwnerMapper.toEntity(request);
        Owner savedOwner = ownerRepository.save(owner);
        return OwnerMapper.toResponse(savedOwner);
    }

    @Override
    public OwnerResponse getOwnerById(Long id) {
        Owner owner = ownerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException( "Owner not found with id: " + id));
        return OwnerMapper.toResponse(owner);
    }

    @Override
    public OwnerResponse getOwnerByFlatNumber(String flatNumber) {
        Owner owner = ownerRepository
                .findByFlatNumber(flatNumber)
                .orElseThrow(() -> new ResourceNotFoundException( "Owner not found with flat number: " + flatNumber));
        return OwnerMapper.toResponse(owner);
    }

    @Override
   @Transactional(readOnly = true)
    public List<OwnerResponse> getAllOwners() {
        List<Owner> owners = ownerRepository.findAll();
        return owners.stream() .map(OwnerMapper::toResponse) .toList();
    }

    @Override
    public OwnerResponse updateOwner(Long id, OwnerRequest request) {
        Owner existingOwner = ownerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException( "Owner not found with id: " + id));
        existingOwner.setName(request.getName());
        existingOwner.setNameInMarathi(request.getNameInMarathi());
        existingOwner.setFlatNumber(request.getFlatNumber());
        existingOwner.setPhoneNo(request.getPhoneNo());
        Owner updatedOwner = ownerRepository.save(existingOwner);
        return OwnerMapper.toResponse(updatedOwner);
    }

    @Override
    public void deleteOwner(Long id) {
        Owner owner = ownerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException( "Owner not found with id: " + id));
        ownerRepository.delete(owner);
    }
}

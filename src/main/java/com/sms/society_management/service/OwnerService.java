package com.sms.society_management.service;

import com.sms.society_management.dto.request.OwnerRequest;
import com.sms.society_management.dto.response.OwnerResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OwnerService {
    OwnerResponse createOwner(OwnerRequest request);
    OwnerResponse getOwnerById(Long id);
    OwnerResponse getOwnerByFlatNumber(String flatNumber);
    List<OwnerResponse> getAllOwners();
    OwnerResponse updateOwner(Long id, OwnerRequest request);
    void deleteOwner(Long id);
}

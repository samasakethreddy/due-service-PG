package com.saketh.due_service.services;

import com.saketh.due_service.dto.DueRequest;
import com.saketh.due_service.dto.DueResponse;
import com.saketh.due_service.dto.SplitBillsRequest;
import com.saketh.due_service.dto.TenantResponse;
import com.saketh.due_service.model.Due;
import com.saketh.due_service.repository.DueRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import com.saketh.due_service.model.Status;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DueService {

    @Autowired
    private DueRepository dueRepository;

    @Value("${api_gateway.service.url}")
    private String apiGatewayURL;

    public Due saveDue(DueRequest dueRequest) {
        Due due = Due.builder()
                .tenantId(dueRequest.getTenantId())
                .amount(dueRequest.getAmount())
                .description(dueRequest.getDescription())
                .dueDate(dueRequest.getDueDate())
                .status(dueRequest.getStatus())
                .build();

        dueRepository.save(due);

        System.out.println("Tenant saved successfully...");

        return due;
    }

    public List<DueResponse> getAllDuesByTenantId(String tenantId) {
        int tenantInt;
        try {
            tenantInt = Integer.parseInt(tenantId); // Convert String to Integer
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid tenant ID format", e);
        }

        List<Due> dues = dueRepository.findByTenantId(tenantInt);
        return dues.stream().map(this::mapToResponse).toList();
    }


    public DueResponse getDueInfoById(Integer id) {
        return dueRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Tenant not found with ID: " + id));
    }


    public List<DueResponse> spitBills(SplitBillsRequest splitBillsRequest, HttpServletRequest request) {
        // Get auth token from request
        String authToken = request.getHeader("Authorization");

        if (authToken == null || authToken.isEmpty()) {
            throw new IllegalArgumentException("Authorization header is required");
        }

        String tenantServiceURL = apiGatewayURL + "/byRoom/" + splitBillsRequest.getRoomId();

        RestClient restClient = RestClient.create();

        // Get tenants from tenant service
        List<TenantResponse> tenants = restClient.get()
                .uri(tenantServiceURL)
                .header("Authorization", authToken)
                .retrieve()
                .body(new ParameterizedTypeReference<List<TenantResponse>>() {
                });

        // If no tenants found or error occurred
        if (tenants == null || tenants.isEmpty()) {
            return List.of();
        }

        // Create dues for each tenant and collect responses
        return tenants.stream()
                .map(tenant -> {
                    // Create due request with fixed amount of 1000
                    DueRequest dueRequest = DueRequest.builder()
                            .tenantId(tenant.getTenantId())  // Assuming tenant.getId() returns UUID
                            .amount(splitBillsRequest.getAmount())
                            .description(splitBillsRequest.getDescription())
                            .dueDate(splitBillsRequest.getDueDate())  // Due in 1 month
                            .status(splitBillsRequest.getStatus())
                            .build();

                    // Save due and map to response
                    Due savedDue = saveDue(dueRequest);
                    return mapToResponse(savedDue);
                })
                .collect(Collectors.toList());
    }


    private DueResponse mapToResponse(Due due) {
        return DueResponse.builder()
                .tenantId(due.getTenantId())
                .amount(due.getAmount())
                .description(due.getDescription())
                .dueDate(due.getDueDate())
                .status(due.getStatus())
                .build();
    }
}

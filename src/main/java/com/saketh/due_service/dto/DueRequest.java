package com.saketh.due_service.dto;

import com.saketh.due_service.model.Status;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DueRequest {

    @NotNull(message = "Tenant ID cannot be null")
    private int tenantId;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @NotNull(message = "Due date cannot be null")
    @FutureOrPresent(message = "Due date cannot be in the past")
    private LocalDate dueDate;

    @NotNull(message = "Status cannot be null")
    private Status status;

}


//{
//        "tenantId": "123e4567-e89b-12d3-a456-426614174000",
//        "amount": 1500.75,
//        "description": "Monthly rent for February",
//        "dueDate": "2025-03-15",
//        "status": "UNPAID"
//        }

package com.saketh.due_service.dto;

import com.saketh.due_service.model.Status;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DueResponse {
	private UUID dueId;
	private int tenantId;
	private BigDecimal amount;
	private String description;
	private LocalDate dueDate;
	private Status status;

}


//{
//        "dueId": "550e8400-e29b-41d4-a716-446655440000",
//        "tenantId": "123e4567-e89b-12d3-a456-426614174000",
//        "amount": 1500.75,
//        "description": "Monthly rent for February",
//        "dueDate": "2025-03-15",
//        "status": "UNPAID"
//        }

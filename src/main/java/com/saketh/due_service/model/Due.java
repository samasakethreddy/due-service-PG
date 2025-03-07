package com.saketh.due_service.model;

import com.saketh.due_service.model.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "Dues")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Due {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID dueId;

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
package com.saketh.due_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantResponse {

	private int tenantId;
	private String tenantName;
	private int tenantAge;
	private int roomId;
	private int aadharId;
	private String email;
	private String phoneNumber;
	private LocalDate joinDate;
}

package com.saketh.due_service.controller;

import com.saketh.due_service.dto.DueRequest;
import com.saketh.due_service.dto.DueResponse;
import com.saketh.due_service.dto.SplitBillsRequest;
import com.saketh.due_service.model.Due;
import com.saketh.due_service.services.DueService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dues")
public class DueController {
    
    @Autowired
    private DueService dueService;

    @PostMapping
    Due createDue(@RequestBody DueRequest dueRequest) {
    	return dueService.saveDue(dueRequest);
    }

    @GetMapping("/allDuesByTenant/{tenant_id}")
    List<DueResponse> getAllDuesByTenantId(@PathVariable String tenantId) {
        return dueService.getAllDuesByTenantId(tenantId);
    }
    
    @GetMapping("/{id}")
    public DueResponse getDueInfo(@PathVariable("id") Integer id) {
        return dueService.getDueInfoById(id);
    }


    @PostMapping("/spitbills/{room_id}")
    List<DueResponse> spitBills(@RequestBody SplitBillsRequest splitBillsRequest, HttpServletRequest request) {
        return dueService.spitBills(splitBillsRequest, request);
    }

}

package com.saketh.due_service.repository;

import com.saketh.due_service.model.Due;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DueRepository extends JpaRepository<Due, Integer> {
    List<Due> findByTenantId(Integer tenantId);
}

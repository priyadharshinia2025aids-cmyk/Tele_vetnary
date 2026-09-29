package com.televet.repository;

import com.televet.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BudgetRepository extends JpaRepository<Budget, Long> {}

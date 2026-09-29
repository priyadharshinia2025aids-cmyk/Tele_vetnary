package com.televet.repository;

import com.televet.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {}

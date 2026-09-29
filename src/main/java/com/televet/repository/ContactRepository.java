package com.televet.repository;

import com.televet.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ContactRepository extends JpaRepository<Contact, Long> {}

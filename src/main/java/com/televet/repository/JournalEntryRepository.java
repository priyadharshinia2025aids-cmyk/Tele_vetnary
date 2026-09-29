package com.televet.repository;

import com.televet.model.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {}

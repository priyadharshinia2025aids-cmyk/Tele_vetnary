package com.televet.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "journal_entries")
public class JournalEntry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String journalType; // SALES, PURCHASE, CASH, BANK
    private String referenceType;
    private Long referenceId;
    private String debitAccount;
    private String creditAccount;
    private BigDecimal amount = BigDecimal.ZERO;
    private LocalDateTime entryDate = LocalDateTime.now();

    public JournalEntry() {}
    public JournalEntry(String journalType,String referenceType,Long referenceId,String debitAccount,String creditAccount,BigDecimal amount){
        this.journalType=journalType; this.referenceType=referenceType; this.referenceId=referenceId;
        this.debitAccount=debitAccount; this.creditAccount=creditAccount; this.amount=amount;
    }
    public Long getId(){return id;}
    public String getJournalType(){return journalType;}
    public String getReferenceType(){return referenceType;}
    public Long getReferenceId(){return referenceId;}
    public String getDebitAccount(){return debitAccount;}
    public String getCreditAccount(){return creditAccount;}
    public BigDecimal getAmount(){return amount;}
    public LocalDateTime getEntryDate(){return entryDate;}
}

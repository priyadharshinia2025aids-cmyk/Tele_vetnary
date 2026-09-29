package com.televet.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultations")
public class Consultation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    private Contact farmer;

    @ManyToOne
    private Contact specialist;

    private LocalDateTime scheduledAt;
    private String livestockType;
    private String symptoms;
    private String diagnosis;
    private String prescription;
    private String diagnosticTests;
    private String priority; // LOW, MEDIUM, HIGH, CRITICAL
    private String status; // SCHEDULED, COMPLETED, CANCELLED
    private LocalDateTime createdAt = LocalDateTime.now();

    public Consultation() {}
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public Contact getFarmer(){return farmer;}
    public void setFarmer(Contact farmer){this.farmer=farmer;}
    public Contact getSpecialist(){return specialist;}
    public void setSpecialist(Contact specialist){this.specialist=specialist;}
    public LocalDateTime getScheduledAt(){return scheduledAt;}
    public void setScheduledAt(LocalDateTime scheduledAt){this.scheduledAt=scheduledAt;}
    public String getLivestockType(){return livestockType;}
    public void setLivestockType(String livestockType){this.livestockType=livestockType;}
    public String getSymptoms(){return symptoms;}
    public void setSymptoms(String symptoms){this.symptoms=symptoms;}
    public String getDiagnosis(){return diagnosis;}
    public void setDiagnosis(String diagnosis){this.diagnosis=diagnosis;}
    public String getPrescription(){return prescription;}
    public void setPrescription(String prescription){this.prescription=prescription;}
    public String getDiagnosticTests(){return diagnosticTests;}
    public void setDiagnosticTests(String diagnosticTests){this.diagnosticTests=diagnosticTests;}
    public String getPriority(){return priority;}
    public void setPriority(String priority){this.priority=priority;}
    public String getStatus(){return status;}
    public void setStatus(String status){this.status=status;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}

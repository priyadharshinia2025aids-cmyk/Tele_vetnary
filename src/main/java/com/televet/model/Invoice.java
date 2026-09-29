package com.televet.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_invoices")
public class Invoice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String invoiceNumber;

    @ManyToOne(optional=false)
    private Contact customer;

    @ManyToOne
    private Consultation consultation;

    private BigDecimal consultationAmount = BigDecimal.ZERO;
    private BigDecimal medicineAmount = BigDecimal.ZERO;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private String paymentStatus = "UNPAID";
    private LocalDateTime invoiceDate = LocalDateTime.now();

    public Invoice() {}
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getInvoiceNumber(){return invoiceNumber;}
    public void setInvoiceNumber(String invoiceNumber){this.invoiceNumber=invoiceNumber;}
    public Contact getCustomer(){return customer;}
    public void setCustomer(Contact customer){this.customer=customer;}
    public Consultation getConsultation(){return consultation;}
    public void setConsultation(Consultation consultation){this.consultation=consultation;}
    public BigDecimal getConsultationAmount(){return consultationAmount;}
    public void setConsultationAmount(BigDecimal v){this.consultationAmount=v;}
    public BigDecimal getMedicineAmount(){return medicineAmount;}
    public void setMedicineAmount(BigDecimal v){this.medicineAmount=v;}
    public BigDecimal getTotalAmount(){return totalAmount;}
    public void setTotalAmount(BigDecimal v){this.totalAmount=v;}
    public String getPaymentStatus(){return paymentStatus;}
    public void setPaymentStatus(String v){this.paymentStatus=v;}
    public LocalDateTime getInvoiceDate(){return invoiceDate;}
}

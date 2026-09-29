package com.televet.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentType; // CUSTOMER_RECEIPT, VENDOR_PAYMENT
    private String method; // CASH, BANK, UPI, CARD
    private BigDecimal amount = BigDecimal.ZERO;
    private String referenceNumber;
    private Long invoiceId;
    private Long vendorBillId;
    private LocalDateTime paidAt = LocalDateTime.now();

    public Payment() {}
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getPaymentType(){return paymentType;}
    public void setPaymentType(String v){this.paymentType=v;}
    public String getMethod(){return method;}
    public void setMethod(String v){this.method=v;}
    public BigDecimal getAmount(){return amount;}
    public void setAmount(BigDecimal v){this.amount=v;}
    public String getReferenceNumber(){return referenceNumber;}
    public void setReferenceNumber(String v){this.referenceNumber=v;}
    public Long getInvoiceId(){return invoiceId;}
    public void setInvoiceId(Long v){this.invoiceId=v;}
    public Long getVendorBillId(){return vendorBillId;}
    public void setVendorBillId(Long v){this.vendorBillId=v;}
    public LocalDateTime getPaidAt(){return paidAt;}
}

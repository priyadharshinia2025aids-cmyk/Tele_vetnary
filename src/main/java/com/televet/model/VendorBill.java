package com.televet.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vendor_bills")
public class VendorBill {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String billNumber;

    @ManyToOne(optional=false)
    private Contact vendor;

    private String purchaseOrderReference;
    private String description;
    private BigDecimal amount = BigDecimal.ZERO;
    private String paymentStatus = "UNPAID";
    private LocalDateTime billDate = LocalDateTime.now();

    public VendorBill() {}
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getBillNumber(){return billNumber;}
    public void setBillNumber(String v){this.billNumber=v;}
    public Contact getVendor(){return vendor;}
    public void setVendor(Contact v){this.vendor=v;}
    public String getPurchaseOrderReference(){return purchaseOrderReference;}
    public void setPurchaseOrderReference(String v){this.purchaseOrderReference=v;}
    public String getDescription(){return description;}
    public void setDescription(String v){this.description=v;}
    public BigDecimal getAmount(){return amount;}
    public void setAmount(BigDecimal v){this.amount=v;}
    public String getPaymentStatus(){return paymentStatus;}
    public void setPaymentStatus(String v){this.paymentStatus=v;}
    public LocalDateTime getBillDate(){return billDate;}
}

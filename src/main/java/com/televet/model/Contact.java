package com.televet.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contacts")
public class Contact {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String contactType; // FARMER, SPECIALIST, LAB_VENDOR

    private String phone;
    private String email;
    private String address;
    private String region;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Contact() {}
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getName(){return name;}
    public void setName(String name){this.name=name;}
    public String getContactType(){return contactType;}
    public void setContactType(String contactType){this.contactType=contactType;}
    public String getPhone(){return phone;}
    public void setPhone(String phone){this.phone=phone;}
    public String getEmail(){return email;}
    public void setEmail(String email){this.email=email;}
    public String getAddress(){return address;}
    public void setAddress(String address){this.address=address;}
    public String getRegion(){return region;}
    public void setRegion(String region){this.region=region;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}

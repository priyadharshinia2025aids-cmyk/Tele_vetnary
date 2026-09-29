package com.televet.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "chart_of_accounts")
public class ChartOfAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String code;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String accountType; // ASSET, LIABILITY, INCOME, EXPENSE

    private BigDecimal balance = BigDecimal.ZERO;

    public ChartOfAccount() {}
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getCode(){return code;}
    public void setCode(String code){this.code=code;}
    public String getName(){return name;}
    public void setName(String name){this.name=name;}
    public String getAccountType(){return accountType;}
    public void setAccountType(String accountType){this.accountType=accountType;}
    public BigDecimal getBalance(){return balance;}
    public void setBalance(BigDecimal balance){this.balance=balance;}
}

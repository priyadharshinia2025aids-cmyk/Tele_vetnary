package com.televet.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "budgets")
public class Budget {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String analyticAccount;

    private String financialYear;
    private String season;
    private BigDecimal allocatedAmount = BigDecimal.ZERO;
    private BigDecimal actualAmount = BigDecimal.ZERO;
    private String region;

    public Budget() {}
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getAnalyticAccount(){return analyticAccount;}
    public void setAnalyticAccount(String v){this.analyticAccount=v;}
    public String getFinancialYear(){return financialYear;}
    public void setFinancialYear(String v){this.financialYear=v;}
    public String getSeason(){return season;}
    public void setSeason(String v){this.season=v;}
    public BigDecimal getAllocatedAmount(){return allocatedAmount;}
    public void setAllocatedAmount(BigDecimal v){this.allocatedAmount=v;}
    public BigDecimal getActualAmount(){return actualAmount;}
    public void setActualAmount(BigDecimal v){this.actualAmount=v;}
    public String getRegion(){return region;}
    public void setRegion(String v){this.region=v;}
    public BigDecimal getUtilization(){return allocatedAmount.signum()==0?BigDecimal.ZERO:actualAmount.multiply(BigDecimal.valueOf(100)).divide(allocatedAmount,2,java.math.RoundingMode.HALF_UP);}
}

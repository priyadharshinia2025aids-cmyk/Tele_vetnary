package com.televet.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String productType; // SERVICE, GOODS

    private String category;
    private BigDecimal price = BigDecimal.ZERO;
    private Integer stockQuantity = 0;
    private String description;

    public Product() {}
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getName(){return name;}
    public void setName(String name){this.name=name;}
    public String getProductType(){return productType;}
    public void setProductType(String productType){this.productType=productType;}
    public String getCategory(){return category;}
    public void setCategory(String category){this.category=category;}
    public BigDecimal getPrice(){return price;}
    public void setPrice(BigDecimal price){this.price=price;}
    public Integer getStockQuantity(){return stockQuantity;}
    public void setStockQuantity(Integer stockQuantity){this.stockQuantity=stockQuantity;}
    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}
}

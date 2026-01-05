package com.example.storageinventory.model;

import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq_gen")
    @SequenceGenerator(name = "product_seq_gen", sequenceName = "PRODUCT_SEQ", allocationSize = 1)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 0;

    @Column(name = "delivery_price")
    private Double deliveryPrice;

    @Column(name = "sale_price")
    private Double salePrice;

    @Column(name = "critical_min")
    private Integer criticalMin;

    public Product() {
    }

    public Product(String productName, Integer quantity, Double deliveryPrice, Double salePrice, Integer criticalMin) {
        this.productName = productName;
        this.quantity = quantity;
        this.deliveryPrice = deliveryPrice;
        this.salePrice = salePrice;
        this.criticalMin = criticalMin;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(Double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getCriticalMin() {
        return criticalMin;
    }

    public void setCriticalMin(Integer criticalMin) {
        this.criticalMin = criticalMin;
    }

    @Override
    public String toString() {
        return productName;
    }
}
package com.example.storageinventory.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@SuppressWarnings("unused")
@Entity
@Table(name = "SALE")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sale_seq_gen")
    @SequenceGenerator(name = "sale_seq_gen", sequenceName = "SALE_SEQ", allocationSize = 1)
    @Column(name = "sale_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "document_number")
    private String documentNumber; // Фактура или касова бележка

    @Column(name = "sale_date")
    private LocalDate saleDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User operator;


    public Sale() {
        this.saleDate = LocalDate.now();
    }

    public Sale(Client client, Product product, int quantity, String documentNumber) {
        this.client = client;
        this.product = product;
        this.quantity = quantity;
        this.documentNumber = documentNumber;
        this.saleDate = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

}
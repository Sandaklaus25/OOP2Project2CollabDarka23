package com.example.storageinventory.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "DELIVERY")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delivery_seq_gen")
    @SequenceGenerator(name = "delivery_seq_gen", sequenceName = "DELIVERY_SEQ", allocationSize = 1)
    @Column(name = "delivery_id")
    private Long id;

    // ВРЪЗКА КЪМ ДОСТАВЧИК: Много доставки могат да са от един доставчик
    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    // ВРЪЗКА КЪМ СТОКА: Много доставки могат да са за една стока
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    // Конструктори
    public Delivery() {
        this.deliveryDate = LocalDate.now(); // По подразбиране - днешна дата
    }

    public Delivery(Supplier supplier, Product product, int quantity, String invoiceNumber) {
        this.supplier = supplier;
        this.product = product;
        this.quantity = quantity;
        this.invoiceNumber = invoiceNumber;
        this.deliveryDate = LocalDate.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public LocalDate getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDate deliveryDate) { this.deliveryDate = deliveryDate; }
}
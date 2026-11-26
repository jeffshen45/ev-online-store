package com.evstore.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import jakarta.persistence.*;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;   // placeholder until checkout needs the real user

    @ManyToOne
    private Vehicle vehicle;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cart_item_selection", joinColumns = @JoinColumn(name = "cart_item_id"))
    @MapKeyColumn(name = "category")
    @Column(name = "chosen")
    private Map<String, String> selections = new HashMap<>();

    private BigDecimal unitPrice;

    public CartItem() {}

    public CartItem(Long customerId, Vehicle vehicle, Map<String, String> selections, BigDecimal unitPrice) {
        this.customerId = customerId;
        this.vehicle = vehicle;
        this.selections = selections;
        this.unitPrice = unitPrice;
    }

    public Long getId() { return id; }
    public Long getCustomerId() { return customerId; }
    public Vehicle getVehicle() { return vehicle; }
    public Map<String, String> getSelections() { return selections; }
    public BigDecimal getUnitPrice() { return unitPrice; }
}
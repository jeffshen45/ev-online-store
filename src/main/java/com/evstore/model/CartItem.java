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

    private String cartOwner;

    @ManyToOne
    private Vehicle vehicle;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cart_item_selection", joinColumns = @JoinColumn(name = "cart_item_id"))
    @MapKeyColumn(name = "category")
    @Column(name = "chosen")
    private Map<String, String> selections = new HashMap<>();

    private BigDecimal unitPrice;

    public CartItem() {}

    public CartItem(String cartOwner, Vehicle vehicle, Map<String, String> selections, BigDecimal unitPrice) {
        this.cartOwner = cartOwner;
        this.vehicle = vehicle;
        this.selections = selections;
        this.unitPrice = unitPrice;
    }

    public Long getId() { return id; }
    public String getCartOwner() { return cartOwner; }
    public Vehicle getVehicle() { return vehicle; }
    public Map<String, String> getSelections() { return selections; }
    public BigDecimal getUnitPrice() { return unitPrice; }
}
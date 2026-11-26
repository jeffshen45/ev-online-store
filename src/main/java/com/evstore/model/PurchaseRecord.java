package com.evstore.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import jakarta.persistence.*;

@Entity
public class PurchaseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    // copied from the vehicle at purchase time
    private String make;
    private String model;
    private int modelYear;
    private int mileage;
    private BigDecimal finalPrice;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "purchase_record_selection", joinColumns = @JoinColumn(name = "purchase_record_id"))
    @MapKeyColumn(name = "category")
    @Column(name = "chosen")
    private Map<String, String> selections = new HashMap<>();

    // payment details
    private String paymentType;        // "FULL" or "LOAN"
    private BigDecimal downPayment;
    private Integer loanMonths;
    private BigDecimal annualRate;
    private BigDecimal monthlyPayment;

    private LocalDateTime purchasedAt;

    public PurchaseRecord() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getModelYear() {
		return modelYear;
	}

	public void setModelYear(int modelYear) {
		this.modelYear = modelYear;
	}

	public int getMileage() {
		return mileage;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	public BigDecimal getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(BigDecimal finalPrice) {
		this.finalPrice = finalPrice;
	}

	public Map<String, String> getSelections() {
		return selections;
	}

	public void setSelections(Map<String, String> selections) {
		this.selections = selections;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public BigDecimal getDownPayment() {
		return downPayment;
	}

	public void setDownPayment(BigDecimal downPayment) {
		this.downPayment = downPayment;
	}

	public Integer getLoanMonths() {
		return loanMonths;
	}

	public void setLoanMonths(Integer loanMonths) {
		this.loanMonths = loanMonths;
	}

	public BigDecimal getAnnualRate() {
		return annualRate;
	}

	public void setAnnualRate(BigDecimal annualRate) {
		this.annualRate = annualRate;
	}

	public BigDecimal getMonthlyPayment() {
		return monthlyPayment;
	}

	public void setMonthlyPayment(BigDecimal monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
	}

	public LocalDateTime getPurchasedAt() {
		return purchasedAt;
	}

	public void setPurchasedAt(LocalDateTime purchasedAt) {
		this.purchasedAt = purchasedAt;
	}

    
}
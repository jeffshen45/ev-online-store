package com.evstore.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evstore.model.CartItem;
import com.evstore.model.Customer;
import com.evstore.model.PurchaseRecord;
import com.evstore.model.Vehicle;
import com.evstore.repository.*;

@Service
public class CheckoutService {

    private final CartItemRepository cartRepo;
    private final VehicleRepository vehicleRepo;
    private final VehicleOptionRepository optionRepo;
    private final CustomerRepository customerRepo;
    private final PurchaseRecordRepository purchaseRepo;
    private final LoanCalculatorService loanCalc;

    public CheckoutService(CartItemRepository cartRepo, VehicleRepository vehicleRepo,
                           VehicleOptionRepository optionRepo, CustomerRepository customerRepo,
                           PurchaseRecordRepository purchaseRepo, LoanCalculatorService loanCalc) {
        this.cartRepo = cartRepo;
        this.vehicleRepo = vehicleRepo;
        this.optionRepo = optionRepo;
        this.customerRepo = customerRepo;
        this.purchaseRepo = purchaseRepo;
        this.loanCalc = loanCalc;
    }

    // One transaction: if anything fails, nothing is saved or deleted
    @Transactional
    public List<PurchaseRecord> checkout(Long customerId, String cartOwner, String paymentType,
            BigDecimal downPercent, int months, BigDecimal annualRate) {
        if (!"FULL".equals(paymentType) && !"LOAN".equals(paymentType)) {
            throw new IllegalArgumentException("Choose a payment type");
        }
        boolean loan = "LOAN".equals(paymentType);
        if (loan && (months < 12 || months > 84)) {
            throw new IllegalArgumentException("Loan term must be 12 to 84 months");
        }
        if (loan && (downPercent.signum() < 0 || downPercent.compareTo(new BigDecimal("90")) > 0)) {
            throw new IllegalArgumentException("Down payment must be 0 to 90 percent");
        }

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        List<CartItem> items = cartRepo.findByCartOwner(cartOwner);
        if (items.isEmpty()) {
            throw new IllegalStateException("Your cart is empty");
        }

        List<PurchaseRecord> records = new ArrayList<>();
        Set<Long> boughtVehicleIds = new HashSet<>();

        for (CartItem item : items) {
            Vehicle v = item.getVehicle();
            if (!boughtVehicleIds.add(v.getId())) {
                continue;   // a vehicle can only be bought once
            }
            PurchaseRecord r = new PurchaseRecord();
            r.setCustomer(customer);
            r.setMake(v.getMake());
            r.setModel(v.getModel());
            r.setModelYear(v.getModelYear());
            r.setMileage(v.getMileage());
            r.setFinalPrice(item.getUnitPrice());
            r.setSelections(new java.util.HashMap<>(item.getSelections()));
            r.setPaymentType(paymentType);
            r.setPurchasedAt(LocalDateTime.now());

            if (loan) {
                BigDecimal down = item.getUnitPrice().multiply(downPercent)
                        .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                r.setDownPayment(down);
                r.setLoanMonths(months);
                r.setAnnualRate(annualRate);
                r.setMonthlyPayment(loanCalc.monthlyPayment(item.getUnitPrice(), down, annualRate, months));
            }
            records.add(purchaseRepo.save(r));
        }

        // remove everything that points at the vehicle, then the vehicle itself
        for (Long vehicleId : boughtVehicleIds) {
            cartRepo.deleteByVehicle_Id(vehicleId);
            optionRepo.deleteByVehicle_Id(vehicleId);
            vehicleRepo.deleteById(vehicleId);
        }
        return records;
    }
}

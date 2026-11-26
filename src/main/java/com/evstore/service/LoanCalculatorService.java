package com.evstore.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;

@Service
public class LoanCalculatorService {

    public BigDecimal monthlyPayment(BigDecimal price, BigDecimal down,
                                     BigDecimal annualRatePercent, int months) {
        if (months <= 0 || price.signum() < 0 || down.signum() < 0 || down.compareTo(price) > 0) {
            throw new IllegalArgumentException("Invalid loan input");
        }
        BigDecimal principal = price.subtract(down);
        if (principal.signum() == 0) {
            return BigDecimal.ZERO.setScale(2);
        }
        double r = annualRatePercent.doubleValue() / 100.0 / 12.0;
        if (r == 0) {
            return principal.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
        }
        double factor = Math.pow(1 + r, months);
        double payment = principal.doubleValue() * r * factor / (factor - 1);
        return BigDecimal.valueOf(payment).setScale(2, RoundingMode.HALF_UP);
    }
}
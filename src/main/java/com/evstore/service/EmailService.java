package com.evstore.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.evstore.model.Customer;
import com.evstore.model.PurchaseRecord;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;   // null until spring.mail.host is set

    public EmailService(ObjectProvider<JavaMailSender> provider) {
        this.mailSender = provider.getIfAvailable();
    }

    public void sendPurchaseConfirmation(Customer customer, List<PurchaseRecord> records) {
        StringBuilder body = new StringBuilder("Hi " + customer.getUsername() + ",\n\nThanks for your purchase:\n\n");
        for (PurchaseRecord r : records) {
            body.append("- ").append(r.getModelYear()).append(' ').append(r.getMake()).append(' ')
                .append(r.getModel()).append(": ").append(r.getFinalPrice());
            if ("LOAN".equals(r.getPaymentType())) {
                body.append(" (").append(r.getMonthlyPayment()).append("/month for ")
                    .append(r.getLoanMonths()).append(" months)");
            }
            body.append('\n');
        }

        if (mailSender == null) {
            log.info("Mail not configured, confirmation not sent to {}:\n{}", customer.getEmail(), body);
            return;
        }
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(customer.getEmail());
            msg.setSubject("EV Store purchase confirmation");
            msg.setText(body.toString());
            mailSender.send(msg);
        } catch (Exception e) {
            log.warn("Could not send confirmation email", e);   // the purchase is already saved
        }
    }
}

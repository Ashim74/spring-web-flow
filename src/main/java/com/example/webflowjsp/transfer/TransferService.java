package com.example.webflowjsp.transfer;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class TransferService {

    public void transfer(TransferForm form) {
        BigDecimal limit = new BigDecimal("100000");
        if (form.getAmount() != null && form.getAmount().compareTo(limit) > 0) {
            throw new RuntimeException("Limit exceeded");
        }
    }
}

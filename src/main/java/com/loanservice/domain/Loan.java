package com.loanservice.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Embeddable
public class Loan {
    private BigDecimal amount;
    private int tenureMonths;
    private LoanPurpose purpose;
}

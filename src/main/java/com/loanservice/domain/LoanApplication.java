package com.loanservice.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Entity
@Data
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID applicationId;
    @Embedded
    private Applicant applicant;
    @Embedded
    private Loan loan;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    @Enumerated(EnumType.STRING)
    private RiskBand riskBand;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<RejectionReason> rejectionReasons;
    private BigDecimal interestRate;
    private BigDecimal emi;
    private BigDecimal totalPayable;
    private LocalDateTime applicationDate;
    public enum ApplicationStatus { APPROVED, REJECTED }
}

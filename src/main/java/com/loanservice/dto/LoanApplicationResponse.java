package com.loanservice.dto;

import com.loanservice.domain.LoanApplication;
import com.loanservice.domain.RejectionReason;
import com.loanservice.domain.RiskBand;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Data @Builder
public class LoanApplicationResponse {
    private UUID applicationId;
    private LoanApplication.ApplicationStatus status;
    private RiskBand riskBand;
    private OfferResponse offer;
    private List<RejectionReason> rejectionReasons;
    @Data @Builder public static class OfferResponse {
        private BigDecimal interestRate;
        private int tenureMonths;
        private BigDecimal emi;
        private BigDecimal totalPayable;
    }
}

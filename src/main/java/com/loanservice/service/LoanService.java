package com.loanservice.service;

import com.loanservice.domain.*;
import com.loanservice.dto.LoanApplicationRequest;
import com.loanservice.dto.LoanApplicationResponse;
import com.loanservice.domain.Applicant;
import com.loanservice.repository.LoanApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service @RequiredArgsConstructor
public class LoanService {
    private final LoanApplicationRepository repository;
    private static final BigDecimal BASE_RATE = new BigDecimal("12.00");
    private static final MathContext MC = new MathContext(10, RoundingMode.HALF_UP);
    public LoanApplicationResponse processApplication(LoanApplicationRequest request) {
        LoanApplication application = new LoanApplication();
        application.setApplicationDate(LocalDateTime.now());
        Applicant applicant = new Applicant();
        applicant.setName(request.getApplicant().getName());
        applicant.setAge(request.getApplicant().getAge());
        applicant.setMonthlyIncome(request.getApplicant().getMonthlyIncome().doubleValue());
        applicant.setEmploymentType(request.getApplicant().getEmploymentType());
        applicant.setCreditScore(request.getApplicant().getCreditScore());
        application.setApplicant(applicant);
        Loan loan = new Loan();
        loan.setAmount(request.getLoan().getAmount());
        loan.setTenureMonths(request.getLoan().getTenureMonths());
        loan.setPurpose(request.getLoan().getPurpose());
        application.setLoan(loan);
        List<RejectionReason> rejectionReasons = new ArrayList<>();
        if (applicant.getCreditScore() < 600) rejectionReasons.add(RejectionReason.CREDIT_SCORE_TOO_LOW);
        if (applicant.getAge() + (double) loan.getTenureMonths() / 12.0 > 65) rejectionReasons.add(RejectionReason.AGE_TENURE_LIMIT_EXCEEDED);
        RiskBand riskBand = classifyRiskBand(applicant.getCreditScore());
        application.setRiskBand(riskBand);
        BigDecimal interestRate = calculateInterestRate(riskBand, applicant.getEmploymentType(), loan.getAmount());
        application.setInterestRate(interestRate);
        BigDecimal emi = calculateEMI(loan.getAmount(), interestRate, loan.getTenureMonths());
        application.setEmi(emi);
        BigDecimal monthlyIncome = BigDecimal.valueOf(applicant.getMonthlyIncome());
        if (emi.compareTo(monthlyIncome.multiply(new BigDecimal("0.60"))) > 0) rejectionReasons.add(RejectionReason.EMI_EXCEEDS_60_PERCENT);
        if (emi.compareTo(monthlyIncome.multiply(new BigDecimal("0.50"))) > 0) rejectionReasons.add(RejectionReason.EMI_EXCEEDS_50_PERCENT);
        if (!rejectionReasons.isEmpty()) {
            application.setStatus(LoanApplication.ApplicationStatus.REJECTED);
            application.setRejectionReasons(rejectionReasons);
            application.setRiskBand(null);
        } else {
            application.setStatus(LoanApplication.ApplicationStatus.APPROVED);
            application.setTotalPayable(emi.multiply(BigDecimal.valueOf(loan.getTenureMonths())).setScale(2, RoundingMode.HALF_UP));
        }
        return mapToResponse(repository.save(application));
    }
    private RiskBand classifyRiskBand(int score) {
        if (score >= 750) return RiskBand.LOW;
        if (score >= 650) return RiskBand.MEDIUM;
        return RiskBand.HIGH;
    }
    private BigDecimal calculateInterestRate(RiskBand band, EmploymentType type, BigDecimal amount) {
        BigDecimal rate = BASE_RATE;
        if (band == RiskBand.MEDIUM) rate = rate.add(new BigDecimal("1.5"));
        else if (band == RiskBand.HIGH) rate = rate.add(new BigDecimal("3.0"));
        if (type == EmploymentType.SELF_EMPLOYED) rate = rate.add(new BigDecimal("1.0"));
        if (amount.compareTo(new BigDecimal("1000000")) > 0) rate = rate.add(new BigDecimal("0.5"));
        return rate.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateEMI(BigDecimal p, BigDecimal r, int n) {
        BigDecimal mRate = r.divide(new BigDecimal("1200"), MC);
        BigDecimal pow = BigDecimal.ONE.add(mRate).pow(n, MC);
        return p.multiply(mRate).multiply(pow).divide(pow.subtract(BigDecimal.ONE), MC).setScale(2, RoundingMode.HALF_UP);
    }

    private LoanApplicationResponse mapToResponse(LoanApplication app) {
        LoanApplicationResponse.LoanApplicationResponseBuilder builder = LoanApplicationResponse.builder()
            .applicationId(app.getApplicationId()).status(app.getStatus()).riskBand(app.getRiskBand()).rejectionReasons(app.getRejectionReasons());
        if (app.getStatus() == LoanApplication.ApplicationStatus.APPROVED) {
            builder.offer(LoanApplicationResponse.OfferResponse.builder().interestRate(app.getInterestRate())
                .tenureMonths(app.getLoan().getTenureMonths()).emi(app.getEmi()).totalPayable(app.getTotalPayable()).build());
        }
        return builder.build();
    }
}

package com.loanservice.service;

import com.loanservice.domain.*;
import com.loanservice.dto.LoanApplicationRequest;
import com.loanservice.dto.LoanApplicationResponse;
import com.loanservice.repository.LoanApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
class LoanServiceTest {
    @Mock private LoanApplicationRepository repository;
    @InjectMocks private LoanService loanService;
    @BeforeEach void setUp() { MockitoAnnotations.openMocks(this); }
    @Test void testEMICalculation() {
        BigDecimal emi = loanService.calculateEMI(new BigDecimal("500000"), new BigDecimal("12.00"), 36);
        assertEquals(new BigDecimal("16607.15"), emi);
    }
    @Test void testApprovedApplication() {
        LoanApplicationRequest request = createValidRequest();
        when(repository.save(any(LoanApplication.class))).thenAnswer(inv -> {
            LoanApplication app = inv.getArgument(0);
            app.setApplicationId(UUID.randomUUID());
            return app;
        });
        LoanApplicationResponse response = loanService.processApplication(request);
        assertEquals(LoanApplication.ApplicationStatus.APPROVED, response.getStatus());
        assertNotNull(response.getOffer());
    }
    private LoanApplicationRequest createValidRequest() {
        LoanApplicationRequest request = new LoanApplicationRequest();
        LoanApplicationRequest.ApplicantRequest applicant = new LoanApplicationRequest.ApplicantRequest();
        applicant.setName("John Doe"); applicant.setAge(30); applicant.setMonthlyIncome(new BigDecimal("75000"));
        applicant.setEmploymentType(EmploymentType.SALARIED); applicant.setCreditScore(750);
        request.setApplicant(applicant);
        LoanApplicationRequest.LoanRequest loan = new LoanApplicationRequest.LoanRequest();
        loan.setAmount(new BigDecimal("500000")); loan.setTenureMonths(36); loan.setPurpose(LoanPurpose.PERSONAL);
        request.setLoan(loan);
        return request;
    }
}

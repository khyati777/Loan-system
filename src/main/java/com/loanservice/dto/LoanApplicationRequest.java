package com.loanservice.dto;

import com.loanservice.domain.EmploymentType;
import com.loanservice.domain.LoanPurpose;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class LoanApplicationRequest {
    @Valid @NotNull private ApplicantRequest applicant;
    @Valid @NotNull private LoanRequest loan;
    @Data public static class ApplicantRequest {
        @NotBlank private String name;
        @Min(21) @Max(60) private int age;
        @DecimalMin("0.01") private BigDecimal monthlyIncome;
        @NotNull private EmploymentType employmentType;
        @Min(300) @Max(900) private int creditScore;
    }
    @Data public static class LoanRequest {
        @DecimalMin("10000.00") @DecimalMax("5000000.00") private BigDecimal amount;
        @Min(6) @Max(360) private int tenureMonths;
        @NotNull private LoanPurpose purpose;
    }
}

package com.loanservice.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Embeddable
public class Applicant {
    private String name;
    private int age;
    private double monthlyIncome;
    private EmploymentType employmentType;
    private int creditScore;
}

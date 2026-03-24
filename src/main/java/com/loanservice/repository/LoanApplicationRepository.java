package com.loanservice.repository;


import com.loanservice.domain.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, UUID> {}

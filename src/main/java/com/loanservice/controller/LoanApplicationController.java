package com.loanservice.controller;

import com.loanservice.dto.LoanApplicationRequest;
import com.loanservice.dto.LoanApplicationResponse;
import com.loanservice.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/applications") @RequiredArgsConstructor

public class LoanApplicationController {
    private final LoanService loanService;
    @PostMapping public ResponseEntity<LoanApplicationResponse> createApplication(@Valid @RequestBody LoanApplicationRequest request) {
        return ResponseEntity.ok(loanService.processApplication(request));
    }
}


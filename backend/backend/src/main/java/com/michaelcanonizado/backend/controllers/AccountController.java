package com.michaelcanonizado.backend.controllers;

import com.michaelcanonizado.backend.dtos.account.AccountCreateDTO;
import com.michaelcanonizado.backend.dtos.account.AccountSummaryDTO;
import com.michaelcanonizado.backend.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/accounts/admin")
    public ResponseEntity<AccountSummaryDTO> createAdmin(@RequestBody @Valid AccountCreateDTO request) {
        AccountSummaryDTO account = accountService.createAdmin(request);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @PostMapping("/accounts/judge")
    public ResponseEntity<AccountSummaryDTO> createJudge(@RequestBody @Valid AccountCreateDTO request) {
        AccountSummaryDTO account = accountService.createJudge(request);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }
}

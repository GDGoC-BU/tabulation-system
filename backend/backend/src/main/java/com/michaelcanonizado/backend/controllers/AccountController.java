package com.michaelcanonizado.backend.controllers;

import com.michaelcanonizado.backend.dtos.account.AccountCreateDTO;
import com.michaelcanonizado.backend.dtos.account.AccountLoginDTO;
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
    private AccountService service;

    @PostMapping("/accounts/login")
    public ResponseEntity<String> loginAccount(@RequestBody @Valid AccountLoginDTO accountLoginDTO) {
        String token = service.loginAccount(accountLoginDTO);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/accounts/admin")
    public ResponseEntity<AccountSummaryDTO> createAdmin(@RequestBody @Valid AccountCreateDTO accountCreateDTO) {
        AccountSummaryDTO account = service.createAdmin(accountCreateDTO);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @PostMapping("/accounts/judge")
    public ResponseEntity<AccountSummaryDTO> createJudge(@RequestBody @Valid AccountCreateDTO accountCreateDTO) {
        AccountSummaryDTO account = service.createJudge(accountCreateDTO);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }
}

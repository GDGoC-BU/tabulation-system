package com.gdgocbu.tabulation.backend.mappers;

import com.gdgocbu.tabulation.backend.dtos.account.AccountCredentialDTO;
import com.gdgocbu.tabulation.backend.dtos.account.AccountSummaryDTO;
import com.gdgocbu.tabulation.backend.models.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountSummaryDTO toSummaryDTO(Account account);
    AccountCredentialDTO toCredentialDTO(Account account);
}

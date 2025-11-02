package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.account.AccountCredentialDTO;
import com.michaelcanonizado.backend.dtos.account.AccountSummaryDTO;
import com.michaelcanonizado.backend.models.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountSummaryDTO toSummaryDTO(Account account);
    AccountCredentialDTO toCredentialDTO(Account account);
}

package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.account.AccountSummaryDTO;
import com.michaelcanonizado.backend.models.Account;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-18T22:48:01+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public AccountSummaryDTO toSummaryDTO(Account account) {
        if ( account == null ) {
            return null;
        }

        UUID id = null;
        String username = null;
        String role = null;
        LocalDateTime lastSeenAt = null;

        id = account.getId();
        username = account.getUsername();
        role = account.getRole();
        lastSeenAt = account.getLastSeenAt();

        boolean isOnline = false;

        AccountSummaryDTO accountSummaryDTO = new AccountSummaryDTO( id, username, role, isOnline, lastSeenAt );

        return accountSummaryDTO;
    }
}

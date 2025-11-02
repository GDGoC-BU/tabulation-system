package com.michaelcanonizado.backend.security;

import com.michaelcanonizado.backend.dtos.account.AccountCredentialDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.UnsupportedAccountTypeException;
import com.michaelcanonizado.backend.mappers.AccountMapper;
import com.michaelcanonizado.backend.models.Account;
import com.michaelcanonizado.backend.repositories.AccountRepository;
import com.michaelcanonizado.backend.services.CacheService;
import com.michaelcanonizado.backend.utilities.AccountTypeConstants;
import com.michaelcanonizado.backend.utilities.CacheNameConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountDetailsService implements UserDetailsService {
    @Autowired
    private AccountRepository repository;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /* Account caching when they log in. NOTE: This method is called on every request,
           therefore adding caching here is important. */
        String CACHE_NAME = CacheNameConstants.ACCOUNT;
        String CACHE_KEY = username + "_credentials";

        /* Account is an abstract class which can't be deserialized, so a DTO will be used.
           This DTO will then be the AccountPrincipal object which means it should hold the
           necessary fields AccountPrincipal needs. */
        AccountCredentialDTO account = cacheService.get(CACHE_NAME, CACHE_KEY, AccountCredentialDTO.class);
        if (account == null) {
            Account accountFromDatabase = repository.findByUsername(username).orElseThrow(() -> {
                return new UsernameNotFoundException("User not found");
            });
            account = accountMapper.toCredentialDTO(accountFromDatabase);
            cacheService.put(CACHE_NAME, CACHE_KEY, account);
        }

        List<String> authorities;
        if (account.accountType().equals(AccountTypeConstants.ADMIN)) {
            authorities = List.of("ADMIN");
        } else if (account.accountType().equals(AccountTypeConstants.JUDGE)) {
            authorities = List.of("JUDGE");
        } else {
            throw new UnsupportedAccountTypeException(
                    "Unknown account type encountered! Please contact admin.",
                    ErrorCode.UNSUPPORTED_ACCOUNT_TYPE
            );
        }

        return new AccountPrincipal(
                account,
                authorities
        );
    }
}

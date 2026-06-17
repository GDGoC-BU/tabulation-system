package com.gdgocbu.tabulation.backend.security;

import com.gdgocbu.tabulation.backend.dtos.account.AccountCredentialDTO;
import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;
import com.gdgocbu.tabulation.backend.exceptions.customs.UnsupportedAccountTypeException;
import com.gdgocbu.tabulation.backend.mappers.AccountMapper;
import com.gdgocbu.tabulation.backend.repositories.AccountRepository;
import com.gdgocbu.tabulation.backend.services.CacheService;
import com.gdgocbu.tabulation.backend.utilities.AccountTypeConstants;
import com.gdgocbu.tabulation.backend.utilities.CacheKeyBuilder;
import com.gdgocbu.tabulation.backend.utilities.CacheNameConstants;
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
    private AccountMapper accountMapper;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private CacheKeyBuilder cacheKeyBuilder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /* This method is called on every request, therefore caching here is important. */
        String CACHE_NAME = CacheNameConstants.AUTH;
        String CACHE_KEY = cacheKeyBuilder.build("accounts", username, "credential");

        /* Account is an abstract class which can't be deserialized, so a DTO will be used.
           This DTO will then be the AccountPrincipal object which means it should hold the
           necessary fields AccountPrincipal needs. */
        AccountCredentialDTO account = cacheService.get(
                CACHE_NAME,
                CACHE_KEY,
                AccountCredentialDTO.class
        );

        if (account == null) {
            account = accountMapper.toCredentialDTO(
                    repository.findByUsername(username).orElseThrow(() -> {
                        return new UsernameNotFoundException("User not found");
                    })
            );
            cacheService.put(
                    CACHE_NAME,
                    CACHE_KEY,
                    account
            );
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

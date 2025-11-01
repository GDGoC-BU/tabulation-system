package com.michaelcanonizado.backend.security;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.UnsupportedAccountTypeException;
import com.michaelcanonizado.backend.models.Account;
import com.michaelcanonizado.backend.models.Admin;
import com.michaelcanonizado.backend.models.Judge;
import com.michaelcanonizado.backend.repositories.AccountRepository;
import com.michaelcanonizado.backend.services.CacheService;
import com.michaelcanonizado.backend.utilities.CacheNameConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountDetailsService implements UserDetailsService {
    private static final String CACHE_NAME = CacheNameConstants.ACCOUNT;

    @Autowired
    private AccountRepository repository;

    @Autowired
    private CacheService cacheService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = cacheService.get(CACHE_NAME, username, Account.class);
        if (account == null) {
            account = repository.findByUsername(username).orElseThrow(() -> {
                return new UsernameNotFoundException("User not found");
            });
            cacheService.put(CACHE_NAME, account.getUsername(), account);
        }

        List<String> authorities;
        if (account instanceof Admin) {
            authorities = List.of("ADMIN");
        } else if (account instanceof Judge) {
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

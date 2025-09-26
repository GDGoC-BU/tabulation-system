package com.michaelcanonizado.backend.security;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.UnsupportedAccountTypeException;
import com.michaelcanonizado.backend.models.Account;
import com.michaelcanonizado.backend.models.Admin;
import com.michaelcanonizado.backend.models.Judge;
import com.michaelcanonizado.backend.repositories.AccountRepository;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = repository.findByUsername(username).orElseThrow(() -> {
            return new UsernameNotFoundException("User not found");
        });

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

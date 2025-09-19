package com.michaelcanonizado.backend.security;

import com.michaelcanonizado.backend.models.Account;
import com.michaelcanonizado.backend.models.Admin;
import com.michaelcanonizado.backend.models.Judge;
import com.michaelcanonizado.backend.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        List<String> roles;
        if (account instanceof Admin) {
            roles = List.of("ADMIN");
        } else if (account instanceof Judge) {
            roles = List.of("JUDGE");
        } else {
            /* Throw proper error */
            throw new UsernameNotFoundException("Unknown account type");
        }

        return new AccountPrincipal(
                account.getId(),
                account.getUsername(),
                account.getPasswordHash(),
                roles
        );
    }
}

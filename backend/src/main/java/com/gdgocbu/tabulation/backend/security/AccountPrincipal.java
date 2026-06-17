package com.gdgocbu.tabulation.backend.security;

import com.gdgocbu.tabulation.backend.dtos.account.AccountCredentialDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AccountPrincipal implements UserDetails {
    private final AccountCredentialDTO account;
    private final List<? extends GrantedAuthority> authorities;
    private final boolean enabled = true;

    public AccountPrincipal(AccountCredentialDTO account, List<String> authorities) {
        this.account = account;
        this.authorities = authorities.stream().map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public AccountCredentialDTO getAccount() {
        return account;
    }

    @Override
    public String getUsername() {
        return account.username();
    }

    @Override
    public String getPassword() {
        return account.passwordHash();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

package com.michaelcanonizado.backend.configurations;

import com.michaelcanonizado.backend.security.filters.JwtFilter;
import com.michaelcanonizado.backend.security.filters.FilterChainExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private FilterChainExceptionHandler filterChainExceptionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                /* Disable CSRF protection */
                .csrf(csrf -> csrf.disable())
                /* Make session stateless */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(request ->
                        request.requestMatchers(
                                /* Allow unauthenticated access to these routes */
                                "api/v1/accounts/login"
                                )
                                .permitAll()
                                /* Everything else, authenticate */
                                .anyRequest()
                                .authenticated()
                )
                /* Allow credentials to be passed in HTTP Headers.
                   I.e: You need this if you want to hit the backend
                   Curl or Postman. */
                .httpBasic(Customizer.withDefaults())
                /* Check for JWTs, else proceed with basic credentials check */
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                /* Ensure that filterChainExceptionHandler is the very first filter
                   to catch filter exceptions. */
                .addFilterBefore(filterChainExceptionHandler, JwtFilter.class)
                .build();
    }

    /* Centralize the password encoder to keep encoding consistent.
       I.e: Using a different value in strength to encode/decode
       will not work! */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        /* Determine the service that will fetch user details */
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        /* Use Bcrypt as password encoder */
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /* Expose AuthenticationManager to the app. Can now be
       used in services. */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }
}

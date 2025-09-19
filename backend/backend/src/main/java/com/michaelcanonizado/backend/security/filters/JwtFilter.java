package com.michaelcanonizado.backend.security.filters;

import com.michaelcanonizado.backend.security.AccountDetailsService;
import com.michaelcanonizado.backend.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        /* If a bearer token is used for authentication */
        String tokenPrefix = "Bearer ";
        if (authorizationHeader != null && authorizationHeader.startsWith(tokenPrefix)) {
            /* Extract token */
            token = authorizationHeader.substring(tokenPrefix.length());
            /* Extract username from token */
            username = jwtService.extractUsername(token);
        }

        /* If a username was extracted, and the request hasn't been authenticated
           by another filter, authenticate it. */
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            /* Fetch account from database */
            UserDetails userDetails =
                    applicationContext
                            .getBean(AccountDetailsService.class)
                            .loadUserByUsername(username);

            /* Validate the token
               (signature, not expired, and matches the expected account) */
            if (jwtService.validateToken(token, userDetails)) {
                /* Create the spring security authentication object */
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                /* Attach other metadata from the request */
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                /* Set the authentication object to SecurityContext.
                   Following filters no longer have to authenticate. */
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        /* Proceed with the next filter */
        filterChain.doFilter(request, response);
    }
}

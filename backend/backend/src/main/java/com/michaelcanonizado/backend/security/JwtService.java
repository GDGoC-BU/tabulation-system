package com.michaelcanonizado.backend.security;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.models.Account;
import com.michaelcanonizado.backend.models.Admin;
import com.michaelcanonizado.backend.models.Judge;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final String secretKey;

    /* Token valid for 4 hours */
    private final long EXPIRATION_MS = 1000 * 60 * 60 * 4;

    public JwtService() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        SecretKey sk = keyGen.generateKey();
        this.secretKey = Encoders.BASE64URL.encode(sk.getEncoded());
    }

    public String generateToken(Account account) {
        String username = account.getUsername();
        String role = getRole(account);
        Map<String, Object> claims = Map.of(
                "role", role
        );

        Date issuedAt = Date.from(Instant.now());
        Date expirationAt = Date.from(Instant.now().plus(Duration.ofMillis(EXPIRATION_MS)));

        return Jwts.builder()
                .subject(username)
                .claims()
                .add(claims)
                .issuedAt(issuedAt)
                .expiration(expirationAt)
                .and()
                .signWith(getKey())
                .compact();
    }

    private String getRole(Account account) {
        if (account instanceof Admin) {
            return "ADMIN";
        } else if (account instanceof Judge) {
            return "JUDGE";
        } else {
            /* THROW CUSTOM EXCEPTION */
            throw new EntityNotFoundException("UNKNOWN ACCOUNT TYPE", ErrorCode.PAGEANT_ACCESS_DENIED);
        }
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(Date.from(Instant.now()));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}

package com.spring.myrestaurant.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@PropertySource("classpath:security.properties")
public class TokenProvider {

    @Value("${jwt.tokenSecret}")
    private String tokenSecret;

    @Value("${jwt.tokenExpirationDays}")
    private long tokenExpirationDays;

    public Jws<Claims> parseToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
        }
        return Jwts.parser()
                .setSigningKey(getSecretKey())
                .parseClaimsJws(token);
    }

    public String createToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date expiryDate = java.sql.Date.valueOf(LocalDate.now().plusDays(tokenExpirationDays));

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSecretKey())
                .compact();
    }

    public Authentication verifyToken(String token) {
        Jws<Claims> claimsJws = parseToken(token);
        Claims body = claimsJws.getBody();
        String username = body.getSubject();

        var roles = (List<String>) body.get("roles");
        Collection<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                authorities
        );
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(tokenSecret.getBytes());
    }

}

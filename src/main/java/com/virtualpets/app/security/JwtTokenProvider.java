package com.virtualpets.app.security;

import com.virtualpets.app.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final UserService userService;
    private final long validityInMilliseconds;

    public JwtTokenProvider (@Lazy UserService userService,
                             @Value("${jwt.secret}") String secret,
                             @Value("${jwt.validity}") long validityInMilliseconds) {
        this.secretKey              = Keys.hmacShaKeyFor(secret.getBytes());
        this.userService            = userService;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken (String username, String role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false; // Millora en el control d’errors
        }
    }

    public String getUsername (String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken (HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer")) ? bearerToken.substring(7) : null;
    }

    public Authentication getAuthentication (String token) {
        String username = getUsername(token);
        UserDetails userDetails = userService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
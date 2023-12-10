package com.medkit.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Getter
public class JwtService {

    private final String secret = "MEDKIT";

    private final int expiration = 20 * 60 * 1000;

    public String generateToken(String userEmail, String userRawPassword) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(userEmail)
                .claim("password", userRawPassword)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);

            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public String getUserEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public String getUserPasswordFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return (String) claims.get("password");
    }
}

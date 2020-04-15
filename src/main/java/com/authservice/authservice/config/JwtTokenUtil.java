package com.authservice.authservice.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenUtil {

    public static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 1000;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * This function is calling doGenerateToken() for token generation
     *
     * @param userDetails
     * @return String
     */
    public String generateToken(UserDetails userDetails) {
        log.info("Entering JwtTokenUtil.generateToken with parameter userDetails {}.", userDetails);
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * This function will generate the token
     *
     * @param claims
     * @param subject
     * @return String
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        log.info("Entering JwtTokenUtil.doGenerateToken with parameters claims {}, subject {}, {}", claims, subject, secret);
        return Jwts.builder().setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 10))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
}

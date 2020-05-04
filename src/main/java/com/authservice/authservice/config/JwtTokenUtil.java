package com.authservice.authservice.config;

import com.authservice.authservice.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class JwtTokenUtil {

    public static final long JWT_TOKEN_VALIDITY_MILLIS = 1000 * 60 * 1000;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String jwtIssuerName;

    /**
     * This function is calling doGenerateToken() for token generation
     *
     * @param userDetails
     * @return String
     */
    public String generateToken(User userDetails) {
        log.info("Entering JwtTokenUtil.generateToken with parameter userDetails {}.", userDetails);
        Map<String, Object> claims = new HashMap<>();
        claims.put("firstName", userDetails.getFirstName());
        claims.put("lastName", userDetails.getLastName());
        claims.put("userId", userDetails.getId());
        return doGenerateToken(claims, userDetails.getUserName());
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
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY_MILLIS))
                .addClaims(claims)
                .setIssuer(jwtIssuerName)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * This function retrieve username from jwt token
     *
     * @param token
     * @return String
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * This function retrieve issuer name from the JWT token
     * @param token
     * @return
     */
    public String getIssuerFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuer);
    }

    /**
     * This function retrieve expiration date from jwt token
     *
     * @param token
     * @return String
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * This function is retrieving any information from token we will need the secret key
     *
     * @param token
     * @return Claims
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * This function check if the token has expired
     *
     * @param token
     * @return Boolean
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        String userNameToken = getUsernameFromToken(token);
        String userNameUserDetails = userDetails.getUsername();
        String issuerNameToken = getIssuerFromToken(token);
        return (userNameToken.equals(userNameUserDetails) && (!isTokenExpired(token)) && (jwtIssuerName.equals(issuerNameToken)));
    }
}

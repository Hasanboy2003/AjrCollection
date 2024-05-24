package com.yurakamri.ajrcollection.security;

import com.yurakamri.ajrcollection.entity.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${jwt.expiration-time}") // 30 kun
    private Long jwtExpirationTime;

    public String generateToken(User user) {
        Claims claims = Jwts
                .claims()
                .setSubject(user.getId().toString());

//        claims.put("userId", user.getId()); subjectda berilgani yetadi

        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("role", user.getRole().getAuthority().toUpperCase().replace("ROLE_", ""));

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtExpirationTime);

        return Jwts
                .builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();
    }

    public UUID getUserIdFromTokenClaims(String token) {
        Claims claims = Jwts
                .parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody();

        String subject = claims.getSubject();
        return UUID.fromString(subject);
    }

    public boolean validateToken(String tokenClient, HttpServletRequest request) {
        try {
            Jwts
                    .parser()
                    .setSigningKey(jwtSecretKey)
                    .parseClaimsJws(tokenClient);
            log.info("Valid token");
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Expired token!");
            request.setAttribute("Expired", e.getMessage());
        } catch (MalformedJwtException malformedJwtException) {
            log.error("Invalid token!");
        } catch (SignatureException s) {
            log.error("Invalid secret key!");
        } catch (UnsupportedJwtException unsupportedJwtException) {
            log.error("Unsupported token!");
        } catch (IllegalArgumentException ex) {
            log.error("Token is blank!");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

}

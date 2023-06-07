package com.kma.project.chatapp.security.jwt;

import com.kma.project.chatapp.entity.UserEntity;
import com.kma.project.chatapp.repository.UserRepository;
import com.kma.project.chatapp.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Autowired
    UserRepository userRepository;
    @Value("${viet.app.jwtSecret}")
    private String jwtSecret;

    @Value("${viet.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Autowired
    BearerTokenInterceptor bearerTokenInterceptor;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userPrincipal.getUsername());
        claims.put("userId", userPrincipal.getUserId());
        claims.put("email", userPrincipal.getEmail());
        claims.put("role", userPrincipal.getAuthorities());

        return Jwts.builder()
//                .setSubject((userPrincipal.getUsername()))
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateTokenFromUsername(String username) {
        Map<String, Object> claims = new HashMap<>();
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
        if (userEntityOptional.isPresent()) {
            claims.put("username", userEntityOptional.get().getUsername());
            claims.put("userId", userEntityOptional.get().getId());
            claims.put("email", userEntityOptional.get().getEmail());
            claims.put("role", userEntityOptional.get().getRoles());
        }
        return Jwts.builder().setClaims(claims).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Long getCurrentUserId() {
        return Long.valueOf(getUserIdFromJwtToken(bearerTokenInterceptor.getBearerToken()));
    }

    public String getUserNameFromJwtToken(String token) {
//        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        return (String) Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("username");

    }

    public String getUserIdFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("userId").toString();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}

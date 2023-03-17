package com.kma.project.chatapp.service.impl;

import com.kma.project.chatapp.dto.request.TokenRefreshRequest;
import com.kma.project.chatapp.dto.response.TokenRefreshResponse;
import com.kma.project.chatapp.entity.RefreshToken;
import com.kma.project.chatapp.entity.UserEntity;
import com.kma.project.chatapp.exception.AppException;
import com.kma.project.chatapp.handler.TokenRefreshException;
import com.kma.project.chatapp.repository.RefreshTokenRepository;
import com.kma.project.chatapp.repository.UserRepository;
import com.kma.project.chatapp.security.jwt.JwtUtils;
import com.kma.project.chatapp.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    JwtUtils jwtUtils;
    @Value("${viet.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = new RefreshToken();
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.username-not-found")).build());
        refreshToken.setUser(userEntity);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest refreshRequest) {
        String requestRefreshToken = refreshRequest.getRefreshToken();

        return findByToken(requestRefreshToken)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = "Bearer " + jwtUtils.generateTokenFromUsername(user.getUsername());
                    return new TokenRefreshResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
}
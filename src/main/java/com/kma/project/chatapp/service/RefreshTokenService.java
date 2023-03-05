package com.kma.project.chatapp.service;

import com.kma.project.chatapp.dto.request.TokenRefreshRequest;
import com.kma.project.chatapp.dto.response.TokenRefreshResponse;
import com.kma.project.chatapp.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    Optional<RefreshToken> findByToken(String token);

    RefreshToken createRefreshToken(String username);

    RefreshToken verifyExpiration(RefreshToken token);

    TokenRefreshResponse refreshToken(TokenRefreshRequest refreshRequest);

}

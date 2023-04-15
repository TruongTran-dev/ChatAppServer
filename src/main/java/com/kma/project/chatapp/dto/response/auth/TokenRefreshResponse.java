package com.kma.project.chatapp.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenRefreshResponse {

    private String accessToken;

    private String refreshToken;

    private String expiredAccessDate;

}

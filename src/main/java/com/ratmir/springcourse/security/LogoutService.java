package com.ratmir.springcourse.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final JwtService jwtService;
    private final TokenRevokeService tokenRevokeService;

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        final String authHeader = request.getHeader("Authorization");
        if(authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            final String jwt = authHeader.substring(7);
            if(!jwt.isBlank()) {
                try {
                    // Извлекаю expiresAt из токена
                    Date expirationDate = jwtService.validateTokenAndGetExpiresAt(request, jwt);

                    // Перевожу токен в HexBinary
                    String jwtInHex = DatatypeConverter.printHexBinary(jwt.getBytes(StandardCharsets.UTF_8));

                    // Отменяю/отключаю токен
                    tokenRevokeService.revokeToken(jwtInHex, expirationDate);
                } catch (Exception e) {
                    try {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                "Invalid JWT in request");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }
}

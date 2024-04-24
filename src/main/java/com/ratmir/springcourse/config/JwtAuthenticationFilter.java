package com.ratmir.springcourse.config;

import com.ratmir.springcourse.security.JwtService;
import com.ratmir.springcourse.security.TokenRevokeService;
import com.ratmir.springcourse.services.PersonDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.bind.DatatypeConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final PersonDetailsService personDetailsService;
    private final TokenRevokeService tokenRevokeService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        if(jwt.isBlank()) {
            handleInvalidJwt(response);
            return;
        }
        try {
            // Перевожу токен в HexBinary и проверяю, что токен не находится в списке отмененных
            String jwtInHex = DatatypeConverter.printHexBinary(jwt.getBytes(StandardCharsets.UTF_8));
            if(tokenRevokeService.isTokenRevoked(jwtInHex)) {
                handleInvalidJwt(response);
                return;
            }

            String username = jwtService.validateTokenAndGetSubject(request, jwt);
            UserDetails userDetails;
            try {
                userDetails = personDetailsService.loadUserByUsername(username);
            } catch (UsernameNotFoundException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Username not found!");
                return;
            }

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            userDetails.getPassword(),
                            userDetails.getAuthorities());

            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            if(SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception exc) {
            handleInvalidJwt(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void handleInvalidJwt(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                "Invalid JWT token provided");
    }
}
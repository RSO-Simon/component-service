package com.component.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtParserService parser;

    public JwtAuthFilter(JwtParserService parser) {
        this.parser = parser;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String p = request.getRequestURI();
        return p.startsWith("/actuator")
                | p.startsWith("/api/component/actuator")
                || p.startsWith("/swagger")
                || p.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            response.setStatus(401);
            return;
        }

        String token = auth.substring("Bearer ".length()).trim();

        try {
            Long ownerUserId = parser.parseOwnerUserId(token);
            AuthContext.setOwnerUserId(ownerUserId);
            chain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(401);
        } finally {
            AuthContext.clear();
        }
    }
}
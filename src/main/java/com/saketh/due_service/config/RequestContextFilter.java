package com.saketh.due_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RequestContextFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestContextFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String rolesHeader = request.getHeader("X-User-Roles");

        logger.debug("RolesHeader: {}", rolesHeader);

        if (!(authentication instanceof JwtAuthenticationToken jwtAuthToken)) {
            logger.debug("Authentication is not a JwtAuthenticationToken, skipping custom role mapping");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Jwt jwt = jwtAuthToken.getToken();
            String email = jwt.getClaimAsString("email");

            logger.debug("Jwt claim email: {}", email);

//            We are not using email in this code, just storing for later user.
            if (email == null) {
                // Try preferred_username if email is not available
                email = jwt.getClaimAsString("preferred_username");
                if (email == null) {
                    logger.warn("No user email or username found in Keycloak JWT");
                    filterChain.doFilter(request, response);
                    return;
                }
            }


//            We are already extracting Roles From Header
//            Get existing authorities from the token
//            Collection<GrantedAuthority> authorities = new ArrayList<>(jwtAuthToken.getAuthorities());
//            authorities.addAll(headerAuthorities);

            List<SimpleGrantedAuthority> headerAuthorities = Arrays.stream(rolesHeader.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            logger.info(headerAuthorities.toString());

            // Create a new JWT authentication with combined authorities
            JwtAuthenticationToken newToken = new JwtAuthenticationToken(
                    jwt,
                    headerAuthorities,
                    jwtAuthToken.getName()
            );

            SecurityContextHolder.getContext().setAuthentication(newToken);

            logger.info("User {} authorized with combined roles: {}", email, headerAuthorities);

        }catch (Exception e) {
            logger.error("Error processing JWT authentication", e);
        }

        filterChain.doFilter(request, response);
    }
}

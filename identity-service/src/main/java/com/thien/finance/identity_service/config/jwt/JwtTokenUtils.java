package com.thien.finance.identity_service.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.thien.finance.identity_service.config.user.UserInfoConfig;
import com.thien.finance.identity_service.model.repository.UserCredentialRepository;

import java.time.Instant;
import java.util.Objects;

/**
 * @author atquil
 */
@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    public String getUserName(Jwt jwtToken){
        return jwtToken.getSubject();
    }

    public boolean isTokenValid(Jwt jwtToken, UserDetails userDetails){
        final String userName = getUserName(jwtToken);
        boolean isTokenExpired = getIfTokenIsExpired(jwtToken);
        boolean isTokenUserSameAsDatabase = userName.equals(userDetails.getUsername());
        return !isTokenExpired  && isTokenUserSameAsDatabase;

    }

    private boolean getIfTokenIsExpired(Jwt jwtToken) {
        return Objects.requireNonNull(jwtToken.getExpiresAt()).isBefore(Instant.now());
    }

    private final UserCredentialRepository userCredentialRepository;
    public UserDetails userDetails(String email){
        return userCredentialRepository
                .findByEmail(email)
                .map(UserInfoConfig::new)
                .orElseThrow(()-> new UsernameNotFoundException("UserEmail: "+email+" does not exist"));
    }
}
package com.thien.finance.identity_service.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.thien.finance.identity_service.dto.UserRegistrationDto;
import com.thien.finance.identity_service.model.entity.UserCredential;

/**
 * @author NguyusernameString, @NotEmpty(message = "tring, @NotEmpty(message = "Thien
 */
@Component
@RequiredArgsConstructor
public class UserInfoMapper {

    private final PasswordEncoder passwordEncoder;
    public UserCredential convertToEntity(UserRegistrationDto userRegistrationDto) {
        UserCredential userInfoEntity = new UserCredential();
        userInfoEntity.setUserName(userRegistrationDto.username());
        userInfoEntity.setEmail(userRegistrationDto.email());
        userInfoEntity.setRoles(userRegistrationDto.role().name());
        userInfoEntity.setPassword(passwordEncoder.encode(userRegistrationDto.password()));
        return userInfoEntity;
    }
}

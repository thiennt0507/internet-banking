package com.thien.finance.identity_service.model.mapper;

import org.springframework.stereotype.Component;

import com.thien.finance.identity_service.dto.UserRegistrationDto;
import com.thien.finance.identity_service.model.dto.UserCreationRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserCreateMapper {
    public UserCreationRequest convertToUserCreationRequest(UserRegistrationDto dto) {
        UserCreationRequest userCreationRequest = new UserCreationRequest();

        userCreationRequest.setUserName(dto.username());
        userCreationRequest.setEmail(dto.email());
        userCreationRequest.setRole(dto.role());

        return userCreationRequest;
    }
}

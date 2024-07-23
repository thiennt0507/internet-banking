package com.thien.finance.identity_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thien.finance.identity_service.exception.EntityNotFoundException;
import com.thien.finance.identity_service.exception.GlobalErrorCode;
import com.thien.finance.identity_service.exception.InvalidEmailException;
import com.thien.finance.identity_service.exception.UserAlreadyRegisteredException;
import com.thien.finance.identity_service.model.dto.Status;
import com.thien.finance.identity_service.model.dto.User;
import com.thien.finance.identity_service.model.dto.UserUpdateRequest;
import com.thien.finance.identity_service.model.entity.UserCredential;
import com.thien.finance.identity_service.model.mapper.UserMapper;
import com.thien.finance.identity_service.model.repository.UserCredentialRepository;
import com.thien.finance.identity_service.model.rest.response.UserResponse;
import com.thien.finance.identity_service.service.rest.BankingCoreRestClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    private UserCredentialRepository repository;

    private final BankingCoreRestClient bankingCoreRestClient;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    private UserMapper userMapper = new UserMapper();

    public String saveUser(UserCredential userCredential) {
        List<UserCredential> userCredentials = repository.findByEmail(userCredential.getEmail());

        if (!userCredentials.isEmpty()) {
            throw new UserAlreadyRegisteredException("This email already registered as a user. Please check and retry", GlobalErrorCode.ERROR_EMAIL_REGISTERED);
        }

        UserResponse userResponse = bankingCoreRestClient.getUser(userCredential.getIdentification());

        if (userResponse.getId() != null) {
            if (!userResponse.getEmail().equals(userCredential.getEmail())) {
                throw new InvalidEmailException("Incorrect email. Please check and retry", GlobalErrorCode.ERROR_INVALID_EMAIL);
            }
        }



        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        repository.save(userCredential);
        return "user added to the system";
    }

    public List<UserCredential> getUsers(Pageable pageable) {
        return repository.findAll(pageable).getContent();
    }

    public User getUser(Long userId) {
        return userMapper.convertToDto(repository.findById(userId).orElseThrow(EntityNotFoundException::new));
    }

    public User updateUser(Long id, UserUpdateRequest userUpdateRequest) {
        UserCredential userCredential = repository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (userUpdateRequest.getStatus() == Status.APPROVED) {
            userCredential.setEnableUser(true);
            userCredential.setVerifyEmail(true);
        }

        userCredential.setStatus(userUpdateRequest.getStatus());
        return userMapper.convertToDto(repository.save(userCredential));
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}

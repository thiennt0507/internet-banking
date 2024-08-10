package com.thien.finance.identity_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thien.finance.identity_service.config.jwt.JwtTokenUtils;
import com.thien.finance.identity_service.dto.ApiResponse;
import com.thien.finance.identity_service.dto.AuthResponseDto;
import com.thien.finance.identity_service.dto.UserRegistrationDto;
import com.thien.finance.identity_service.dto.ValidateReponse;
import com.thien.finance.identity_service.model.dto.AuthRequest;
import com.thien.finance.identity_service.model.entity.UserCredential;
import com.thien.finance.identity_service.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/bank-users")
public class AuthController {
    private final AuthService authService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtils jwtTokenUtils ;


    @PostMapping("/sign-in")
    public ApiResponse<?> authenticateUser(Authentication authentication,HttpServletResponse response){
        ApiResponse<AuthResponseDto> apiReponse = new ApiResponse<>();
        // throw new RuntimeException("Authentication");
        apiReponse.setMessage("You are logged in");
        apiReponse.setResult(authService.getJwtTokensAfterAuthentication(authentication,response));
        return apiReponse;
    }

    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @PostMapping ("/refresh-token")
    public ResponseEntity<?> getAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        log.info("[AuthController:getAccessToken]Refresh Token Process Started for user:{}",authorizationHeader);
        return ResponseEntity.ok(authService.getAccessTokenUsingRefreshToken(authorizationHeader));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto,
                                          BindingResult bindingResult, HttpServletResponse httpServletResponse){

        log.info("[AuthController:registerUser] Signup Process Started for user:{}",userRegistrationDto.username());
        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            log.error("[AuthController:registerUser]Errors in user:{}",errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        return ResponseEntity.ok(authService.registerUser(userRegistrationDto,httpServletResponse));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto,
                                          BindingResult bindingResult, HttpServletResponse httpServletResponse){

        log.info("[AuthController:registerUser] Signup Process Started for user:{}",userRegistrationDto.username());
        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            log.error("[AuthController:registerUser]Errors in user:{}",errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        return ResponseEntity.ok(authService.registerUser(userRegistrationDto,httpServletResponse));
    }

    
    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return authService.generateToken(authRequest.getUsername());
        } else {
            throw new RuntimeException("invalid access");
        }
    }

    @GetMapping("/validate")
    public ApiResponse<ValidateReponse> validateToken(@RequestParam("token") String token) {
        try {
            authService.validateToken(token);
            
            return ApiResponse.<ValidateReponse>builder().result(ValidateReponse.builder().valid(true).build()).build();
        } catch (Exception e) {
            return ApiResponse.<ValidateReponse>builder().result(ValidateReponse.builder().valid(false).build()).build();
            // TODO: handle exception
        }
    
    }
}

package com.thien.finance.identity_service.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.thien.finance.identity_service.config.RSAKeyRecord;
import com.thien.finance.identity_service.config.jwt.JwtTokenGenerator;
import com.thien.finance.identity_service.config.jwt.JwtTokenUtils;
import com.thien.finance.identity_service.dto.AuthResponseDto;
import com.thien.finance.identity_service.dto.TokenType;
import com.thien.finance.identity_service.dto.UserRegistrationDto;
import com.thien.finance.identity_service.exception.EntityNotFoundException;
import com.thien.finance.identity_service.exception.GlobalErrorCode;
import com.thien.finance.identity_service.exception.InvalidEmailException;
import com.thien.finance.identity_service.exception.UserAlreadyRegisteredException;
import com.thien.finance.identity_service.model.dto.Role;
import com.thien.finance.identity_service.model.dto.Status;
import com.thien.finance.identity_service.model.dto.User;
import com.thien.finance.identity_service.model.dto.UserCreationRequest;
import com.thien.finance.identity_service.model.dto.UserUpdateRequest;
import com.thien.finance.identity_service.model.entity.RefreshTokenEntity;
import com.thien.finance.identity_service.model.entity.UserCredential;
import com.thien.finance.identity_service.model.mapper.UserCreateMapper;
import com.thien.finance.identity_service.model.mapper.UserInfoMapper;
import com.thien.finance.identity_service.model.mapper.UserMapper;
import com.thien.finance.identity_service.model.repository.RefreshTokenRepo;
import com.thien.finance.identity_service.model.repository.UserCredentialRepository;
import com.thien.finance.identity_service.model.repository.httpclient.CoreBankingClient;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserCredentialRepository userCredentialRepository;

    private final RSAKeyRecord rsaKeyRecord;
    
    private final JwtTokenUtils jwtTokenUtils;
    
    private final UserCreateMapper userCreateMapper;

    private final CoreBankingClient coreBankingClient;

    private final RefreshTokenRepo refreshTokenRepo;

    private final JwtTokenGenerator jwtTokenGenerator;

    private final UserInfoMapper userInfoMapper;
    
    private final PasswordEncoder passwordEncoder;

    private final JWTService jwtService;

    private UserMapper userMapper = new UserMapper();


    public AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) {
        try
        {
            System.out.println(authentication.getName());
            var userCredential = userCredentialRepository.findByUserName(authentication.getName())
                    .orElseThrow(()->{
                        log.error("[AuthService:userSignInAuth] User: {} not found", authentication.getName());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND,"USER NOT FOUND ");});

            

            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            log.info("[AuthService:userSignInAuth] Refresh token: {}", refreshToken);
            //Let's save the refreshToken as well
            saveUserRefreshToken(userCredential,refreshToken);
            //Creating the cookie
            creatRefreshTokenCookie(response,refreshToken);

            log.info("[AuthService:userSignInAuth] Access token for user:{}, has been generated",userCredential.getUserName());
            return  AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(15 * 60)
                    .userName(userCredential.getUserName())
                    .tokenType(TokenType.Bearer)
                    .build();


        }catch (Exception e){
            log.error("[AuthService:userSignInAuth]Exception while authenticating the user due to :" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please Try Again");
        }
    }

    private void saveUserRefreshToken(UserCredential userCredential, String refreshToken) {
        var refreshTokenEntity = RefreshTokenEntity.builder()
                .user(userCredential)
                .refreshToken(refreshToken)
                .revoked(false)
                .build();
        refreshTokenRepo.save(refreshTokenEntity);
    }

    private Cookie creatRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token",refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60 ); // in seconds
        response.addCookie(refreshTokenCookie);
        return refreshTokenCookie;
    }


    public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {

        if(!authorizationHeader.startsWith(TokenType.Bearer.name())){
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please verify your token type");
        }

        final String refreshToken = authorizationHeader.substring(7);

        //Find refreshToken from database and should not be revoked : Same thing can be done through filter.
        var refreshTokenEntity = refreshTokenRepo.findByRefreshToken(refreshToken)
                .filter(tokens-> !tokens.isRevoked())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Refresh token revoked"));

        UserCredential userCredential = refreshTokenEntity.getUser();

        //Now create the Authentication object
        Authentication authentication =  createAuthenticationObject(userCredential);

        //Use the authentication object to generate new accessToken as the Authentication object that we will have may not contain correct role.
        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

        return  AuthResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(5 * 60)
                .userName(userCredential.getUserName())
                .tokenType(TokenType.Bearer)
                .build();
    }

    private static Authentication createAuthenticationObject(UserCredential userCredential) {
        // Extract user details from UserDetailsEntity
        String username = userCredential.getEmail();
        String password = userCredential.getPassword();
        String roles = userCredential.getRoles();

        // Extract authorities from roles (comma-separated)
        String[] roleArray = roles.toString().split(",");
        GrantedAuthority[] authorities = Arrays.stream(roleArray)
                .map(role -> (GrantedAuthority) role::trim)
                .toArray(GrantedAuthority[]::new);

        return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(authorities));
    }

    public AuthResponseDto registerUser(UserRegistrationDto userRegistrationDto,
                                        HttpServletResponse httpServletResponse) {
        try{
            log.info("[AuthService:registerUser]User Registration Started with :::{}",userRegistrationDto);

            Optional<UserCredential> user = userCredentialRepository.findByEmail(userRegistrationDto.email());
            Optional<UserCredential> user1 = userCredentialRepository.findByUserName(userRegistrationDto.username());

            if(user.isPresent() || user1.isPresent()) {
                throw new Exception("User Already Exist");
            }

            UserCredential userDetailsEntity = userInfoMapper.convertToEntity(userRegistrationDto);
            Authentication authentication = createAuthenticationObject(userDetailsEntity);


            // Generate a JWT token
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            UserCredential savedUserDetails = userCredentialRepository.save(userDetailsEntity);

            saveUserRefreshToken(userDetailsEntity,refreshToken);

            creatRefreshTokenCookie(httpServletResponse, refreshToken);

            log.info("[AuthService:registerUser] User:{} Successfully registered",savedUserDetails.getUserName());

            // Fetch data to Banking core service
            UserCreationRequest userRequest = userCreateMapper.convertToUserCreationRequest(userRegistrationDto);
            String userResponse = coreBankingClient.createUser(userRequest);

            log.info(userResponse.toString());

            return   AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(5 * 60)
                    .userName(savedUserDetails.getUserName())
                    .tokenType(TokenType.Bearer)
                    .build();


        } catch (Exception e){
            log.error("[AuthService:registerUser]Exception while registering the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    public List<UserCredential> getUsers(Pageable pageable) {
        return userCredentialRepository.findAll(pageable).getContent();
    }

    public User getUser(Long userId) {
        return userMapper.convertToDto(userCredentialRepository.findById(userId).orElseThrow(EntityNotFoundException::new));
    }

    public User updateUser(Long id, UserUpdateRequest userUpdateRequest) {
        UserCredential userCredential = userCredentialRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (userUpdateRequest.getStatus() == Status.APPROVED) {
            userCredential.setEnableUser(true);
        }

        userCredential.setStatus(userUpdateRequest.getStatus());
        return userMapper.convertToDto(userCredentialRepository.save(userCredential));
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public boolean validateToken(String token) {
        // jwtService.validateToken(token);
        JwtDecoder jwtDecoder =  NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();

        Jwt jwtToken = jwtDecoder.decode(token);

        final String userName = jwtTokenUtils.getUserName(jwtToken);

        if (!userName.isEmpty()) {
            UserDetails userDetails = jwtTokenUtils.userDetails(userName);
            
            if (jwtTokenUtils.isTokenValid(jwtToken,userDetails)) {
                return true;
            }

            return false;
        }

        return false;
        
    }
}

package com.thien.finance.core_banking_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.thien.finance.core_banking_service.model.dto.User;
import com.thien.finance.core_banking_service.model.dto.UserRegister;
import com.thien.finance.core_banking_service.model.entity.UserEntity;
import com.thien.finance.core_banking_service.model.mapper.UserMapper;
import com.thien.finance.core_banking_service.repository.UserRepository;
import com.thien.finance.core_banking_service.service.impl.BaseRedisServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
// @RequiredArgsConstructor
public class UserService extends BaseRedisServiceImpl{

    public UserService(RedisTemplate<String, Object> redisTemplate, UserRepository userRepository) {
        super(redisTemplate);
        this.userRepository = userRepository;
    }

    private static final String KEY_ALL_USER = "allusers";

    
    private UserMapper userMapper = new UserMapper();

    private final UserRepository userRepository;

    public User readMe(Authentication authentication) {
        String username = authentication.getName();
        Optional<UserEntity> userEntity = userRepository.findByUserName(username);
        
        
        if (!userEntity.isPresent()) {
            throw new EntityNotFoundException("User not found");
        }

        return userMapper.convertToDto(userEntity.get());
    }

    public void createUser(UserRegister userRegister) {
        UserEntity user =  new UserEntity();
        user.setUserName(userRegister.getUserName());
        user.setRole(userRegister.getRole().name());
        user.setEmail(userRegister.getEmail());
        userRepository.save(user);
    }

    public boolean updateUser(UserRegister userRegister) {
        Optional<UserEntity> userEntity = userRepository.findByUserName(userRegister.getUserName());

        
        if (!userEntity.isPresent()) {
            return false;
        }
        
        userEntity.get().setEmail(userRegister.getEmail());
        userEntity.get().setRole(userRegister.getRole().name());
        userRepository.save(userEntity.get());
        
        return true;
        
    }

    public User readUser(String username, Authentication authentication)  {
        String currentUsername = authentication.getName();

        log.info(username + currentUsername);

        if (!username.equals(currentUsername) && authentication.getAuthorities().stream().noneMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
            throw new IllegalArgumentException("You are not authorized to view this user's information");
        }

        Optional<UserEntity> userEntity = userRepository.findByUserName(username);
        if (!userEntity.isPresent()) {
            throw new EntityNotFoundException("User not found");
        }

        return userMapper.convertToDto(userEntity.get());
    }
    
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN' ,'ROLE_MANAGER')")
    public List<User> readUsers(Pageable pageable) {

        List<User> users = new ArrayList<User>();
        
        Boolean isHasDataInRedis = this.keyExists(KEY_ALL_USER);

        System.out.println(isHasDataInRedis);

        if (isHasDataInRedis) {
            log.info("Get data in redis");
           users = (List<User>) this.get(KEY_ALL_USER);
           this.setTimeToLive(KEY_ALL_USER, 1L);

           return users;
        }

        users = userMapper.convertToDtoList(userRepository.findAll(pageable).getContent());
        log.info("Get data in database");
        this.set(KEY_ALL_USER, users);

        return users;
    }
}

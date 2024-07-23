package com.thien.finance.identity_service.model.mapper;

import org.springframework.beans.BeanUtils;

import com.thien.finance.identity_service.model.dto.User;
import com.thien.finance.identity_service.model.entity.UserCredential;

public class UserMapper extends BaseMapper<UserCredential, User>{
    @Override
    public UserCredential convertToEntity(User dto, Object... args) {
        UserCredential userCredential = new UserCredential();
        if (dto != null) {
            BeanUtils.copyProperties(dto, userCredential);
        }
        return userCredential;
    }

    @Override
    public User convertToDto(UserCredential entity, Object... args) {
        User user = new User();
        if (entity != null) {
            BeanUtils.copyProperties(entity, user);
        }
        return user;
    }
}
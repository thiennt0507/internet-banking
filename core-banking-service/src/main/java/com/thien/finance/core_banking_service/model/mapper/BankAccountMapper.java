package com.thien.finance.core_banking_service.model.mapper;

import org.springframework.beans.BeanUtils;

import com.thien.finance.core_banking_service.model.dto.BankAccount;
import com.thien.finance.core_banking_service.model.dto.User;
import com.thien.finance.core_banking_service.model.entity.BankAccountEntity;

public class BankAccountMapper extends BaseMapper<BankAccountEntity, BankAccount> {

    @Override
    public BankAccountEntity convertToEntity(BankAccount dto, Object... args) {
        BankAccountEntity entity = new BankAccountEntity();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity, "user");
        }
        return entity;
    }

    @Override
    public BankAccount convertToDto(BankAccountEntity entity, Object... args) {
        BankAccount dto = new BankAccount();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto, "user");
            User userDto = new User();
            userDto.setId(entity.getUser().getId());
            userDto.setUserName(entity.getUser().getUserName());
            userDto.setEmail(entity.getUser().getEmail());

            dto.setUser(userDto);
        }
        return dto;
    }
}

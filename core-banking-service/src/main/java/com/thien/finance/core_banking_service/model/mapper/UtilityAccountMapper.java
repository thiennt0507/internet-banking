package com.thien.finance.core_banking_service.model.mapper;

import org.springframework.beans.BeanUtils;

import com.thien.finance.core_banking_service.model.dto.UtilityAccount;
import com.thien.finance.core_banking_service.model.entity.UtilityAccountEntity;

public class UtilityAccountMapper extends BaseMapper<UtilityAccountEntity, UtilityAccount> {
    @Override
    public UtilityAccountEntity convertToEntity(UtilityAccount dto, Object... args) {
        UtilityAccountEntity entity = new UtilityAccountEntity();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    @Override
    public UtilityAccount convertToDto(UtilityAccountEntity entity, Object... args) {
        UtilityAccount dto = new UtilityAccount();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}

package com.thien.finance.fun_transfer_service.model.mapper;

import org.springframework.beans.BeanUtils;

import com.thien.finance.fun_transfer_service.model.dto.FundTransfer;
import com.thien.finance.fun_transfer_service.model.entity.FundTransferEntity;

public class FundTransferMapper extends BaseMapper<FundTransferEntity, FundTransfer> {
    @Override
    public FundTransferEntity convertToEntity(FundTransfer dto, Object... args) {
        FundTransferEntity entity = new FundTransferEntity();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    @Override
    public FundTransfer convertToDto(FundTransferEntity entity, Object... args) {
        FundTransfer dto = new FundTransfer();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}

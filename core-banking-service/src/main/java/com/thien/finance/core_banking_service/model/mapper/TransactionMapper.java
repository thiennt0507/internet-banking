package com.thien.finance.core_banking_service.model.mapper;

import org.springframework.beans.BeanUtils;

import com.thien.finance.core_banking_service.model.dto.BankAccount;
import com.thien.finance.core_banking_service.model.dto.Transaction;
import com.thien.finance.core_banking_service.model.entity.TransactionEntity;

public class TransactionMapper extends BaseMapper<TransactionEntity, Transaction> {

    @Override
    public TransactionEntity convertToEntity(Transaction dto, Object... args) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToEntity'");
    }

    @Override
    public Transaction convertToDto(TransactionEntity entity, Object... args) {
       Transaction dto = new Transaction();

       if (entity != null  ) {
            BeanUtils.copyProperties(entity, dto);

            BankAccount bankAccountDto = new BankAccount();
            bankAccountDto.setNumber(entity.getAccount().getNumber());
            bankAccountDto.setId(entity.getAccount().getId());

            dto.setAccount(bankAccountDto);
       }

       return dto;
    }
    
}

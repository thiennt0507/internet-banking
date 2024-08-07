package com.thien.finance.core_banking_service.service;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.thien.finance.core_banking_service.model.AccountStatus;
import com.thien.finance.core_banking_service.model.AccountType;
import com.thien.finance.core_banking_service.model.dto.BankAccount;
import com.thien.finance.core_banking_service.model.dto.User;
import com.thien.finance.core_banking_service.model.dto.UtilityAccount;
import com.thien.finance.core_banking_service.model.entity.BankAccountEntity;
import com.thien.finance.core_banking_service.model.entity.UserEntity;
import com.thien.finance.core_banking_service.model.entity.UtilityAccountEntity;
import com.thien.finance.core_banking_service.model.mapper.BankAccountMapper;
import com.thien.finance.core_banking_service.model.mapper.UserMapper;
import com.thien.finance.core_banking_service.model.mapper.UtilityAccountMapper;
import com.thien.finance.core_banking_service.repository.BankAccountRepository;
import com.thien.finance.core_banking_service.repository.UserRepository;
import com.thien.finance.core_banking_service.repository.UtilityAccountRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private BankAccountMapper bankAccountMapper = new BankAccountMapper();
    private UserMapper userMapper = new UserMapper();
    private UtilityAccountMapper utilityAccountMapper = new UtilityAccountMapper();

    private final BankAccountRepository bankAccountRepository;
    private final UtilityAccountRepository utilityAccountRepository;
    private final UserRepository userRepository;

    public BankAccount readBankAccount(String accountNumber) {
        BankAccountEntity entity = bankAccountRepository.findByNumber(accountNumber).orElseThrow(EntityNotFoundException::new);
        return bankAccountMapper.convertToDto(entity);
    }

    public BankAccount createBankAccount(Authentication authentication, BankAccount bankAccountDto) {
        String userName = authentication.getName();
        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow(EntityNotFoundException::new);

        if (userEntity == null)  {
            throw new IllegalArgumentException("User not found or was deleted: " + userName);
        }

        if (bankAccountRepository.findByNumber(bankAccountDto.getNumber()).isPresent()) {
            throw new EntityExistsException("Bank account has already been created for " + bankAccountDto.getNumber());
        }

        System.out.println(bankAccountDto);

        BankAccountEntity bankAccountEntity = new BankAccountEntity();

        bankAccountEntity.setNumber(bankAccountDto.getNumber());
        bankAccountEntity.setType(AccountType.SAVINGS_ACCOUNT);
        bankAccountEntity.setStatus(AccountStatus.ACTIVE);
        bankAccountEntity.setActualBalance(bankAccountDto.getActualBalance());
        bankAccountEntity.setAvailableBalance(bankAccountDto.getAvailableBalance());
        bankAccountEntity.setUser(userEntity);

        BankAccountEntity savedBankAcountEntity = bankAccountRepository.save(bankAccountEntity);

        return bankAccountMapper.convertToDto(savedBankAcountEntity);
    }

    public List<BankAccount> readBankAccountsMe(Authentication authenticaton, String currentUserName) {
        String userName = authenticaton.getName();

        if (!userName.equals(currentUserName)) {
            log.info(userName + " and " + currentUserName);
            throw new IllegalArgumentException("You are not authorized to perform this operation");
        }

        Optional<UserEntity> user = userRepository.findByUserName(userName);

        if (!user.isPresent()) {
            throw new IllegalArgumentException("User not found or was deleted: " + userName);
        }

        Optional<List<BankAccountEntity>> entity = bankAccountRepository.findByUserId(user.get().getId());

        if (!entity.isPresent()) {
            throw new NotFoundException("BankAccount not found");
        }

        return bankAccountMapper.convertToDtoList(entity.get());
    }

    public BankAccount readBankAccountMe(Authentication authentication, String userName, String numberAccount) {
        List<BankAccount> bankAccounts = readBankAccountsMe(authentication, userName);

        Optional<BankAccount> bankAccount =  bankAccounts.stream().parallel().filter(account -> account.getNumber().equals(numberAccount)).findAny();

        if (!bankAccount.isPresent()) {
            throw new NotFoundException("BankAccount not found");
        }
        
        return bankAccount.get();
    }

    public UtilityAccount readUtilityAccount(String provider) {
        UtilityAccountEntity utilityAccountEntity = utilityAccountRepository.findByProviderName(provider).orElseThrow(EntityNotFoundException::new);
        return utilityAccountMapper.convertToDto(utilityAccountEntity);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UtilityAccount createUtilityAccount(Authentication authentication, UtilityAccount utilityAccountDto) {
        Optional<UserEntity> userEntity = userRepository.findByUserName(authentication.getName());

        if (!userEntity.isPresent()) {
            throw new IllegalStateException("User not found with username " + authentication.getName() );
        }

        if (utilityAccountRepository.findByProviderName(utilityAccountDto.getProviderName()).isPresent() 
            ||
            utilityAccountRepository.findByNumber(utilityAccountDto.getNumber()).isPresent()
        ) {
            throw new IllegalStateException("Utitily Account has beeen created  with user  " +
                      utilityAccountRepository.findByProviderName(utilityAccountDto.getProviderName()).get().getUserEntityUpdated().getUserName());
        }

        UtilityAccountEntity utilityAccountEntity = new  UtilityAccountEntity() ;
        utilityAccountEntity.setNumber(utilityAccountDto.getNumber());
        utilityAccountEntity.setProviderName(utilityAccountDto.getProviderName());
        utilityAccountEntity.setUserEntityUpdated(userEntity.get());

        System.out.println(userEntity.get().getUserName());

        UtilityAccountEntity savedUtilityAccountEntity =  utilityAccountRepository.save(utilityAccountEntity);

        return utilityAccountMapper.convertToDto(savedUtilityAccountEntity);

    }

    public List<UtilityAccount> readUtilityAccounts() {
        List<UtilityAccountEntity> utilityAccounts = utilityAccountRepository.findAll();
        
        return utilityAccounts.stream().map(utilityAccountMapper::convertToDto).collect(Collectors.toList());
    }

    public UtilityAccount readUtilityAccount(Long id) {
        return utilityAccountMapper.convertToDto(utilityAccountRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }
}

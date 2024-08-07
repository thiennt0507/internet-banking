package com.thien.finance.core_banking_service.model.dto;

import lombok.Data;

@Data
public class UtilityAccount {
    private Long id;
    private String number;
    private String providerName;
    private User userUpdated;
}

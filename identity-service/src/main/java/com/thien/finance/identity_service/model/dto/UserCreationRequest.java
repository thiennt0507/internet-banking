package com.thien.finance.identity_service.model.dto;

import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class UserCreationRequest {
    String userName;
    String email;
    Role role;
}

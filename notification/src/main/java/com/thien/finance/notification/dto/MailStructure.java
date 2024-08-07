package com.thien.finance.notification.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class MailStructure {
    private String subject;
    private String message;
}

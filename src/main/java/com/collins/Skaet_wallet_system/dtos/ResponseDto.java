package com.collins.Skaet_wallet_system.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ResponseDto<T> {
    private String statusCode;
    private String statusMessage;
    private T data;
}

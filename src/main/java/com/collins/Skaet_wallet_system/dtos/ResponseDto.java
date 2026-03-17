package com.collins.Skaet_wallet_system.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDto<T> {
    private String statusCode;
    private String statusMessage;
    private T data;
}

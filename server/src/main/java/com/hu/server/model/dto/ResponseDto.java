package com.hu.server.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {
    String code = "200";
    String message = "success";
    T data;

    public ResponseDto(T data) {
        this.data = data;
    }
}

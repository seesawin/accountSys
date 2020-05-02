package com.seesawin.payload.response;

import lombok.Data;

@Data
public class CommonResponse<T> {
    String code;
    String msg;
    T data;
}

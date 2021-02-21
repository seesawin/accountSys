package com.seesawin.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderdetailResponse {
    private String name;

    private Integer count;

    private Integer price;
}
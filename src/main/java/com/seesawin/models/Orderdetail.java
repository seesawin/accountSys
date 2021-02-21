package com.seesawin.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Orderdetail {
    private Integer id;

    private Integer orderno;

    private String name;

    private Integer count;

    private Integer price;
}
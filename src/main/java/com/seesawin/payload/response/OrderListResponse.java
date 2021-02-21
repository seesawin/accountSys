package com.seesawin.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderListResponse {
    private int orderNo;
    private String username;
    private int totalPrice;
    private List<OrderdetailResponse> orderDetails;
}

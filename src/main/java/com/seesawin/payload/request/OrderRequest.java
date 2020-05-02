package com.seesawin.payload.request;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class OrderRequest {
    @NotBlank
    private String username;

    private List<OrderDetailRequest> orderDetialRequestList;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<OrderDetailRequest> getOrderDetialRequestList() {
        return orderDetialRequestList;
    }

    public void setOrderDetialRequestList(List<OrderDetailRequest> orderDetialRequestList) {
        this.orderDetialRequestList = orderDetialRequestList;
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "username='" + username + '\'' +
                ", orderDetialRequestList=" + orderDetialRequestList +
                '}';
    }
}

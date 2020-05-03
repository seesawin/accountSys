package com.seesawin.payload.response;

import java.util.List;

public class OrderListResponse {
    private int orderNo;
    private String username;
    private int totalPrice;
    private List<OrderdetailResponse> orderDetails;

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OrderdetailResponse> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderdetailResponse> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "OrderListResponse{" +
                "orderNo=" + orderNo +
                ", username='" + username + '\'' +
                ", totalPrice=" + totalPrice +
                ", orderDetails=" + orderDetails +
                '}';
    }
}

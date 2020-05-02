package com.seesawin.payload.request;

import javax.validation.constraints.NotBlank;

public class OrderDetailRequest {
    @NotBlank
    private String name;
    @NotBlank
    private int count;
    @NotBlank
    private int price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderDetailRequest{" +
                "name='" + name + '\'' +
                ", count=" + count +
                ", price=" + price +
                '}';
    }
}

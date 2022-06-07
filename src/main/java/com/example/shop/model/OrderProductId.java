package com.example.shop.model;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class OrderProductId implements Serializable {
    @Column(name = "product_id")
    private int productId;

    @Column(name = "order_id")
    private int orderId;

    public int getOrderId() {
        return orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int product) {
        this.productId = product;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public OrderProductId(int productId, int orderId) {
        this.productId = productId;
        this.orderId = orderId;
    }



    public OrderProductId() {
    }
}

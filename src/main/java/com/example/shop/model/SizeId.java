package com.example.shop.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class SizeId implements Serializable {

    private int productId;

    private int size;

    public int getSize() {
        return size;
    }

    public int getProductId() {
        return productId;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public SizeId(int productId, int size) {
        this.productId = productId;
        this.size = size;
    }
    public SizeId(Product product, int size) {
        this.productId = product.getId();
        this.size = size;
    }

    public SizeId() {
    }
}
package com.example.shop.model;

public class SizeDto {

    private Product product;
    private int size;
    private int number;

    public SizeDto(Product product, int size, int number) {
        this.product = product;
        this.size = size;
        this.number = number;
    }

    public SizeDto() {
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public int getNumber() {
        return number;
    }

    public Product getProduct() {
        return product;
    }
}

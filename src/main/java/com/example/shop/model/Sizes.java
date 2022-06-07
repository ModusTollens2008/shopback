package com.example.shop.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "sizes")
public class Sizes {
    @EmbeddedId
    private SizeId id;
    @Column(name = "number")
    private int number;


    @MapsId("productId")
    @JoinColumn(name = "product_id",referencedColumnName="id")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    public Product getProduct() {
        return product;
    }

    public SizeId getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public Sizes(SizeId id, int number, Product product) {
        this.id = id;
        this.number = number;
        this.product = product;
    }

    public Sizes(Product product,int size,int number) {
        id=new SizeId(product.getId(),size);
        this.number = number;
        this.product = product;
    }



    public void setId(SizeId id) {
        this.id = id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Sizes() {
    }
}

package com.example.shop.model;


import javax.persistence.*;

@Entity
@Table(name="order_products")
public class OrderProduct {

    @EmbeddedId
    private OrderProductId id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product;

    @Column(name="chosensize")
    int chosensize;


    public void setOrder(Order order) {
        this.order = order;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setChosensize(int chosensize) {
        this.chosensize = chosensize;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public int getChosensize() {
        return chosensize;
    }

    public OrderProduct(int chosensize)
    {
        this.chosensize=chosensize;
    }
    public OrderProduct(){}

    public OrderProduct(Order order, Product product, int chosensize) {
        this.order = order;
        this.product = product;
        this.chosensize = chosensize;
    }
}

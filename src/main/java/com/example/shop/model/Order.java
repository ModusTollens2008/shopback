package com.example.shop.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "status")
    private String status;
    @Column(name="date")
    private Date date;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(	name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products = new HashSet<>();

//    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "order")
//    private Set<OrderProduct> orderProducts = new HashSet<>();

    public Order(User user, String status, Set<Product> products) {
        this.user = user;
        this.status = status;
        this.products = products;
        java.sql.Date sqlDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
        this.date=sqlDate;
    }
    public Order(){
        java.sql.Date sqlDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
        this.date=sqlDate;
        this.status="ORDERED";
    }


    public void setUser(User user) {
        this.user = user;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }

    public Date getDate() {
        return date;
    }

    public Set<Product> getProducts() {
        return products;
    }
}

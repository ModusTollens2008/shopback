package com.example.shop.repo;

import com.example.shop.model.Order;
import com.example.shop.model.OrderProduct;
import com.example.shop.model.OrderProductId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderProductRepo extends CrudRepository<OrderProduct, OrderProductId>
{
    List<OrderProduct> findByOrderId(int id);
}

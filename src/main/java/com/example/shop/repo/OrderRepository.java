package com.example.shop.repo;

import com.example.shop.model.Order;
import com.example.shop.model.Product;
import com.example.shop.model.User;
import org.hibernate.sql.Delete;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.SQLException;
import java.util.List;

public interface OrderRepository extends CrudRepository<Order,Integer> {
    List<Order> findByUserId(int id);
    @Query("SELECT o FROM Order o order by o.id")
    List<Order> findByOrder();
    Order findById(int id);
    @Modifying
    @Query(value="DELETE from order_products WHERE product_id=:productid AND order_id=:id",nativeQuery = true)
    void deleteOrderProduct(@Param("productid")int productid,@Param("id")int id);

    @Modifying
    @Query(value="DELETE from order_products WHERE order_id=:id",nativeQuery = true)
    void deleteProductsinOrder(@Param("id")int id);
    @Modifying
    @Query(value = "INSERT into order_products(order_id,product_id) values(:id , :productid)",nativeQuery = true)
    void postOrderProduct(@Param("id")int id,@Param("productid") int productid);
}
package com.example.shop.repo;

import com.example.shop.model.Category;
import com.example.shop.model.Product;
import com.example.shop.model.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product,Integer> {
    List<Product> findByName(String name);
    List<Product> findByBrandInAndCategoryInAndPriceBetween(String[] brands, String[] category, double price1, double price2);
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    List<Product> findByNameIsLike(@Param("name") String name);
    @Query("SELECT p FROM Product p order by p.id")
    List<Product> findByOrder();

}

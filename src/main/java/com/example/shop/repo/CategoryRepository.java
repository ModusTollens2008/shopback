package com.example.shop.repo;

import com.example.shop.model.Category;
import com.example.shop.model.ERole;
import com.example.shop.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Role,Long> {
    Optional<Category> findByName(String name);

}

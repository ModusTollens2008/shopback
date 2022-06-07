package com.example.shop.repo;

import com.example.shop.model.Role;
import com.example.shop.model.SizeId;
import com.example.shop.model.Sizes;

import org.springframework.data.repository.CrudRepository;

public interface SizeRepository extends CrudRepository<Sizes, SizeId> {

}

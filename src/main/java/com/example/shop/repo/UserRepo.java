package com.example.shop.repo;

import com.example.shop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepo extends CrudRepository<User,Long> {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    List<User> findAll();

    @Query(value = "SELECT u from User u where u.id=1")
    User find();

    @Query("SELECT u FROM User u order by u.id")
    List<User> findByOrderBy();

    @Query(value = "INSERT INTO user_roles(user_id,role_id) values(:userid,:roleid)",nativeQuery = true)
    void insertUserRole(@Param("userid") int userid,@Param("roleid")  int roleid);
}

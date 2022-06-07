package com.example.shop.controller;


import com.example.shop.model.*;
import com.example.shop.repo.*;
import com.example.shop.security.services.UserDetailsImpl;
import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import payload.MessageResponse;
import payload.SignupRequest;
import org.postgresql.util.PSQLException;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/mod")
public class ModeratorController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepo userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    SizeRepository sizeRepository;
    @Autowired
    RoleRepository roleRepository;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @PostMapping("/product")
    public ResponseEntity<?> postProduct(@RequestBody Product product) {
        Product pr = null;
        try {
            pr = productRepository.findByName(product.getName()).get(0);
        } catch (Exception e) {
            pr = null;
        }

        if (pr == null) {
            productRepository.save(product);
            return ResponseEntity.ok(new MessageResponse("Товар успешно добавлен!"));
        } else if (!pr.equals(product)) {
            productRepository.save(product);
            return ResponseEntity.ok(new MessageResponse("Товар успешно добавлен!"));
        } else return ResponseEntity.badRequest().body(new MessageResponse("Товар с таким названием уже существует!"));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @PutMapping("/product")
    public ResponseEntity<?> upProduct(@RequestBody Product product) {
        productRepository.save(product);
        return ResponseEntity.ok(new MessageResponse("Товар успешно изменён!"));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @DeleteMapping("/product")
    public void deleteProduct(@RequestParam(name = "id", required = true) int id) {
        productRepository.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("/products")
    public List<Product> getProducts() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetailsImpl) principal).getUsername();
        System.out.println(username);
        return productRepository.findByOrder();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findByOrderBy();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("/orders")
    public List<Order> getOrders() {
        return orderRepository.findByOrder();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("/order")
    public ResponseEntity<?> upOrder(@RequestParam(name = "id", required = true) int id,
                                     @RequestParam(name = "status", required = true) String status) {
        Order order = orderRepository.findById(id);
        order.setStatus(status);
        orderRepository.save(order);
        return ResponseEntity.ok(new MessageResponse("Товар успешно изменён!"));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("/orderdetails")
    public Set<Product> getOrderDetails(@RequestParam(name = "id", required = true) int id) {
        Order order = orderRepository.findById(id);

        return order.getProducts();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @Transactional
    @DeleteMapping("/orderdetails")
    public void deleteOrderDetails(@RequestParam(name = "productid", required = true) int productid, @RequestParam(name = "id", required = true) int id) {
        orderRepository.deleteOrderProduct(productid, id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @Transactional
    @DeleteMapping("/order")
    public void deleteOrder(@RequestParam(name = "id", required = true) int id) {
        orderRepository.deleteProductsinOrder(id);
        orderRepository.deleteById(id);
    }

   // @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @Transactional
    @GetMapping("/orderproduct")
    public ResponseEntity<?> postOrderProduct(@RequestParam(name = "id", required = true) int id,
                                              @RequestParam(name = "productid", required = true) int productid) {
        orderRepository.postOrderProduct(id, productid);
        return ResponseEntity.ok(new MessageResponse("Товар успешно добавлен"));

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @PostMapping("/order")
    public ResponseEntity<?> postOrder(@RequestBody Order order) {
        orderRepository.save(order);
        return ResponseEntity.ok(new MessageResponse("Заказ успешно создан!"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/user")
    public ResponseEntity<?> putUser(@RequestBody User user) {

        try {
            User ur = userRepository.findById(user.getId()).get();
            ur.setUsername(user.getUsername());
            ur.setEmail(user.getEmail());
            ur.setNumber(user.getNumber());
            ur.setAddress(user.getAddress());
            userRepository.save(ur);
            return ResponseEntity.ok(new MessageResponse("Пользователь успешно изменен!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Пользователь с таким именеи уже существует!"));
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(@RequestParam(name = "id",required = true) int id)
    {
        userRepository.deleteById(Long.valueOf(id));
        return ResponseEntity.ok("ok");
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @PostMapping("/sizes")
    public ResponseEntity<?> postSizes(@RequestBody SizeDto sizeDto) {

        Sizes size = new Sizes(sizeDto.getProduct(), sizeDto.getSize(), sizeDto.getNumber());
        sizeRepository.save(size);
        return ResponseEntity.ok(new MessageResponse("Успешно добавлено!"));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("/sizes")
    public Iterable<Sizes> getSizes() {
        //sizeRepository.save(new Sizes(productRepository.findById(1).get(),43,34));
        return sizeRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @DeleteMapping("/sizes")
    public void deleteSize(@RequestParam(name = "productId") int id, @RequestParam(name = "size") int size) {
        sizeRepository.deleteById(new SizeId(id, size));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/userroles")
    public ResponseEntity<?> upRoles(@RequestParam(name = "userid") int userid, @RequestParam(name = "roleid") int roleid) {
        User user = userRepository.findById(Long.valueOf(userid)).get();
        Set<Role> newroles = new HashSet<>();
        newroles.add(roleRepository.findById(roleid).get());
        user.setRoles(newroles);
        userRepository.save(user);
        return ResponseEntity.ok("sssssssss");
    }

}

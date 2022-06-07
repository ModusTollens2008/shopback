package com.example.shop.controller;

import com.example.shop.DAO.UserDao;
import com.example.shop.model.*;
import com.example.shop.repo.ProductRepository;
import com.example.shop.repo.RoleRepository;
import com.example.shop.repo.UserRepo;
import com.example.shop.security.jwt.JwtUtils;
import com.example.shop.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import payload.JwtResponse;
import payload.LoginRequest;
import payload.MessageResponse;
import payload.SignupRequest;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepo userRepository;
    @Autowired
    ProductRepository productRepositorry;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));
        SecurityContextHolder.
                getContext().
                setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser( @RequestBody SignupRequest signUpRequest) {
        System.out.println(roleRepository.count());
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Пользователь с таким именем уже существует!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().
                    body(new MessageResponse("Пользователь с такой почтой уже существует!"));
        }
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getPhonenumber(),
                signUpRequest.getAddress());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Ошибка: такой роли нет!"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Ошибка: такой роли нет!"));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Ошибка: такой роли нет!"));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Ошибка: такой роли нет!"));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Пользователь успешно зарегистрирован!"));
    }
    @GetMapping("/users")
    public List<User> printHello()
    {

        User mod = userRepository.findById(4L).get();
        Role role = roleRepository.findByName(ERole.ROLE_ADMIN).get();
        mod.getRoles().remove(role);
        userRepository.save(mod);
        return userRepository.findAll();

    }
    @GetMapping("/products")
    @ResponseBody
    public List<Product> getProducts(@RequestParam(value = "category", required=false)String[] category,
                                     @RequestParam(value = "brands",required = false) String[] brands,
                                   @RequestParam(value = "price", required = true) String[] price)
    {

        return  productRepositorry.findByBrandInAndCategoryInAndPriceBetween(brands,category,Double.parseDouble(price[0]),
                Double.parseDouble(price[1]));

    }

    @GetMapping("/search")
    @ResponseBody
    public List<Product> productSearch(@RequestParam(value = "name", required=true)String name)
    {
        return  productRepositorry.findByNameIsLike(name);

    }

    @PostMapping("/order")
    public ResponseEntity<?> postOrder(@RequestBody List<Product> products)
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetailsImpl)principal).getUsername();
        System.out.println(username);
        if(userRepository.existsByUsername(username)) {
            User user = userRepository.findByUsername(username).get();
            Order order = new Order();
            order.setUser(user);
            Set<Product> user_products = new HashSet<>();


            for (Product pr :
                    products) {
                user_products.add(productRepositorry.findById(pr.getId()).get());
                System.out.println(pr.getName());

            }
            order.setProducts(user_products);
            user.getOrders().add(order);
            userRepository.save(user);
            return ResponseEntity.ok("Ok");
        }
        else
            return ResponseEntity.badRequest().body("User not found");
    }
}


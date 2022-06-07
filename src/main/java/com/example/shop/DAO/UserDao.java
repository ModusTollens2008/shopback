package com.example.shop.DAO;

import com.example.shop.model.User;
import com.example.shop.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Repository
public class UserDao  {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public User getMovie(Long movieId) {
        //EntityManager em = Persistence.createEntityManagerFactory("default").createEntityManager();
        User movie = em.find(User.class, new Long(movieId));
        em.detach(movie);
        return movie;
    }
}

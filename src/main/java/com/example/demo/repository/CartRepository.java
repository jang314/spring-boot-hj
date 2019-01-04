package com.example.demo.repository;

import com.example.demo.entity.Cart;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartRepository extends CrudRepository<Cart, Long> {
    List<Cart> findByUid(String uid);
    Cart findByProdId(Long id);
}

package com.example.demo.repository;

import com.example.demo.entity.OrderProduct;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderProdRepository extends CrudRepository<OrderProduct, Long> {
//    List<OrderProduct> findByOrder(Long id);
}
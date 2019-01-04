package com.example.demo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class OrderDTO {
    private Long id;
    private String orderId;
    private Integer totalPrice;
    private String status;
    private String uid;
    private List<ProductDTO> products;
}

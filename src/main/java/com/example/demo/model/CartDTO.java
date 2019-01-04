package com.example.demo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CartDTO {
    private Long id;
    private Long prodId;
    private String prodName;
    private Integer prodPrice;
    private Integer amount;
    private Integer totalPrice;
}

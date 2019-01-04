package com.example.demo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class ProductDTO {
    private Long prodId;
    private String prodName;
    private Integer price;
    private Integer amount;
}

package com.example.demo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CartForm {
    private String uid;
    private Long prodId;
    private Integer amount;

}

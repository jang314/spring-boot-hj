package com.example.demo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Data
@Getter
@Setter
public class OrderForm {
    private ArrayList<Long> id;
    private ArrayList<Integer> amount;
    private Integer totalPrice;
}

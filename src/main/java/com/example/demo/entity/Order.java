package com.example.demo.entity;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "tbl_order")
@EqualsAndHashCode(of="oid")
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true, name = "oid")
    private String oId;

    @Column(nullable = false)
    private String uid;

    @CreationTimestamp
    private Date orderdate;

    @Column
    private Integer totalPrice;

    @Column(nullable = false, length = 100)
    private String status;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="oid")
    private List<OrderProduct> products;

}

package com.example.demo.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "order_prod")
@EqualsAndHashCode(of="id")
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Integer amount;
    @Column(name = "prod_id")
    private Long prodId;

    @ManyToOne
    @JoinColumn(name = "oid", nullable = false)
    private Order order;
}

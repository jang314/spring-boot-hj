package com.example.demo.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 100)
    private String pname;

    @Column(nullable = false, length = 200)
    private String content;

    @Column(nullable = false, length = 50)
    private Integer stock;

    @Column(nullable = false)
    private Integer price;

    @CreationTimestamp
    private Date regdate;

    @Column(nullable = false)
    private String file;

}

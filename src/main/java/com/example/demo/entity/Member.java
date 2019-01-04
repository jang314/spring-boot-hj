package com.example.demo.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
@EqualsAndHashCode(of="uid")
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "아이디는 공백일 수 없습니다. ")
    @Column(nullable = false, unique = true, length = 50)
    private String uid;

    @NotNull(message = "비밀번호는 공백일 수 없습니다. ")
    @Column(nullable = false, length = 200)
    private String upw;

    @Column(nullable = false, length = 50)
    private String uname;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @CreationTimestamp
    private Date regdate;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="uid")
    private List<MemberRole> roles;
}

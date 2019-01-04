package com.example.demo.repository;

import com.example.demo.entity.Member;
import org.springframework.data.repository.CrudRepository;


public interface MemberRepository extends CrudRepository<Member ,Long> {
    Member findByUid(String uid);
}

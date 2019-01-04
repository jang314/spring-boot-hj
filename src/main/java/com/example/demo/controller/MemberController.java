package com.example.demo.controller;

import com.example.demo.entity.Member;
import com.example.demo.entity.MemberRole;
import com.example.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    MemberRepository memberRepository;



    @PostMapping
    public String addMember(@Valid Member member, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            for(ObjectError error : bindingResult.getAllErrors()){
                return error.getDefaultMessage();
            }
        }else{
            MemberRole role = new MemberRole();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            member.setUpw(passwordEncoder.encode(member.getUpw()));
            role.setRoleName("MEMBER");
            member.setRoles(Arrays.asList(role));
            memberRepository.save(member);
            return "success";
        }
        return "";
    }

    @PutMapping
    public void updateMember(@RequestBody Member member){
        Member member1 = memberRepository.findByUid(member.getUid());
        if(member.getUpw().length()==0){
            member.setUpw(member1.getUpw());
        }
        member1.setUid(member.getUid());
        member1.setUpw(member.getUpw());
        member1.setAddress(member.getAddress());
        member1.setPhone(member.getPhone());
        member1.setUname(member.getUname());
        memberRepository.save(member1);
    }

    @GetMapping("/{id}")
    public Boolean existsById(@PathVariable String id){
        Member member = memberRepository.findByUid(id);
        if(member==null)    return false;
        else return true;
    }

    @DeleteMapping
    public boolean outMember(@RequestBody Member member){
        if(member != null){
            memberRepository.deleteById(memberRepository.findByUid(member.getUid()).getId());
            return true;
        }else{
            return false;
        }
    }

    @GetMapping("getupw/{upw}")
    public boolean getUpw(@PathVariable String upw){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if(passwordEncoder.matches(upw, memberRepository.findByUid(auth.getName()).getUpw())){
            return true;
        }else{
            return false;
        }
    }


}

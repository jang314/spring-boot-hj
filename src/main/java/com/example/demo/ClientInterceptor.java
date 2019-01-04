package com.example.demo;

import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

@Component
@Slf4j
public class ClientInterceptor implements HandlerInterceptor {
    @Autowired
    MemberRepository memberRepository;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUid(auth.getName());
        Boolean isAuth =false;
        if(member==null){
            modelAndView.addObject("isAuth", false);
        }else{
            modelAndView.addObject("isAuth", true);
            modelAndView.addObject("member", member);
            modelAndView.addObject("isAdmin", false);
            for(int i = 0 ; i < member.getRoles().size(); i++){
                modelAndView.addObject("isAdmin", true);
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception{
        log.info("afterCompletion()");
    }

}

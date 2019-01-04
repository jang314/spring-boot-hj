package com.example.demo.controller;

import com.example.demo.entity.Member;
import com.example.demo.model.OrderForm;
import com.example.demo.repository.MemberRepository;
import com.sun.deploy.net.HttpResponse;
import com.sun.deploy.net.URLEncoder;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.ArrayList;

@Controller
public class ViewController {

    @Autowired
    MemberRepository memberRepository;

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String main(Model model){
        isAuthencation(model);
        return "index.html";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup.html";
    }

//    @GetMapping("/login")
//    public String loginForm(HttpServletRequest req){
//        String referer = req.getHeader("Referer");
//        req.getSession().setAttribute("prevPage", referer);
//        return "login.html";
//    }

    @GetMapping("mypage")
    public String modify(Model model){
        isAuthencation(model);
        return "mypage.html";
    }

    @GetMapping("cart")
    public String cart(Model model){
//        isAuthencation(model);
        return "cart.html";
    }

    //    admin
    @GetMapping("admin")
    public String admin(Model model){
        isAuthencation(model);
        return "admin/index.html";
    }

    @GetMapping("admin/plist")
    public String plist(Model model){
        isAuthencation(model);
        return "admin/plist.html";
    }

    @GetMapping("admin/upload")
    public String upload(Model model){
        isAuthencation(model);
        return "admin/upload.html";
    }

    @GetMapping("admin/view/{id}")
    public String view(Model model, @PathVariable("id") Long id){
        isAuthencation(model);
        model.addAttribute("id", id);
        return "admin/pdetail.html";
    }

    @GetMapping("admin/update/{id}")
    public String update(Model model, @PathVariable("id") Long id){
        isAuthencation(model);
        model.addAttribute("id", id);
        return "admin/pinfo.html";
    }

    @GetMapping("plist")
    public String cplist(Model model){
        isAuthencation(model);
        return "plist.html";
    }

    @GetMapping("pinfo/{id}")
    public String detailView(@PathVariable("id") Long id, Model model){
        isAuthencation(model);
        model.addAttribute("id", id);
        return "pinfo.html";
    }


    @GetMapping("oinfo")
    public String getOrderView(HttpSession session, Model model){
        isAuthencation(model);
        session.getAttribute("form");
        session.getAttribute("totalPrice");
        model.addAttribute("form", session.getAttribute("form"));
        model.addAttribute("totalPrice", session.getAttribute("totalPrice"));
        System.out.println(session.getAttribute("form"));
        return "oinfo.html";
    }

    @GetMapping("olist")
    public String orderList(Model model){
        isAuthencation(model);
        return "olist.html";
    }

    @GetMapping("admin/olist")
    public String adminOrder(Model model){
        isAuthencation(model);
        return "admin/olist.html";
    }

    public void isAuthencation(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUid(auth.getName());
        Boolean isAuth = false;
        if(member!=null){
            model.addAttribute("member", member);
            model.addAttribute("isAuth", true);
            model.addAttribute("isAdmin", false);
            for(int i = 0 ; i < member.getRoles().size(); i++){
                if(member.getRoles().get(i).getRoleName().equals("ADMIN")){
                    model.addAttribute("isAdmin", true);
                }
            }
        }
    }
}

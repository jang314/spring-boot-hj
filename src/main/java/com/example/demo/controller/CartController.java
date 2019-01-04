package com.example.demo.controller;

import com.example.demo.entity.Cart;
import com.example.demo.entity.Product;
import com.example.demo.model.CartDTO;
import com.example.demo.model.CartForm;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductRepository productRepository;

    @PostMapping
    public void addCart(@RequestBody CartForm form){
        Cart cart = new Cart();
        cart.setProdId(form.getProdId());
        cart.setAmount(form.getAmount());
        cart.setUid(form.getUid());

        cartRepository.save(cart);
    }

    @GetMapping("{uid}")
    public List<CartDTO> getCart(@PathVariable String uid){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getName().equals(uid)){
            List<Cart> list = cartRepository.findByUid(auth.getName());
            List<CartDTO> carts = new ArrayList<>();
            System.out.println(list);
            for(int i=0; i <list.size(); i++){
                Optional<Product> product = productRepository.findById(list.get(i).getProdId());
                CartDTO dto = new CartDTO();
                Integer totalPrice = 0;
                totalPrice+=product.get().getPrice();
                dto.setId(list.get(i).getId());
                dto.setProdId(product.get().getId());
                dto.setProdName(product.get().getPname());
                dto.setProdPrice(product.get().getPrice());
                dto.setAmount(list.get(i).getAmount());
                dto.setTotalPrice(totalPrice);
                carts.add(i, dto);
                System.out.println(dto);

            }
            return carts;
        }else{
            return null;
        }

    }

    @GetMapping("/isCart/{id}")
    public Boolean isCart(@PathVariable Long id){
        if(cartRepository.findByProdId(id)==null){
            return false;
        }else{
            return true;
        }
    }


    @DeleteMapping
    public void deleteCart(@RequestBody CartDTO dto){
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("sdfsdfsdf  "+auth.getName());
        Optional<Cart> cart = cartRepository.findById(dto.getId());
        if (cart.get().getUid().equals(auth.getName())){
            cartRepository.deleteById(dto.getId());
        }else{
            return ;
        }
    }

    @PutMapping
    public void putCart(@RequestBody CartDTO dto){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<Cart> cart;
        if(dto.getAmount()!=null){
            cart = cartRepository.findById(dto.getId());
            cart.get().setAmount(dto.getAmount());
        }else{
            cart = cartRepository.findById(cartRepository.findByProdId(dto.getProdId()).getId());
            cart.get().setAmount(cartRepository.findByProdId(dto.getProdId()).getAmount() + 1);
        }
        cartRepository.save(cart.get());
    }
}

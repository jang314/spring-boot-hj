package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    OrderProdRepository orderProdRepository;

    @Autowired
    MemberRepository memberRepository;

    @PostMapping
    public void addOrder(@RequestBody OrderForm form, HttpSession session){
        List<CartDTO> carts = new ArrayList<>();

        for(int i = 0 ; i <form.getId().size(); i++){
            Optional<Cart> cart = cartRepository.findById(form.getId().get(i));
            Optional<Product> product = productRepository.findById(cart.get().getProdId());
            CartDTO cartdto = new CartDTO();
            cartdto.setProdId(product.get().getId());
            cartdto.setProdName(product.get().getPname());
            cartdto.setProdPrice(product.get().getPrice());
            cartdto.setAmount(cart.get().getAmount());
            cartdto.setTotalPrice(product.get().getPrice()*cart.get().getAmount());
            carts.add(i, cartdto);
        }
        Integer totalPrice = 0;
        for(int i = 0; i< carts.size(); i++){
            totalPrice +=carts.get(i).getTotalPrice();
        }
        session.setAttribute("form" , carts);
        session.setAttribute("totalPrice", totalPrice);
    }

    @PostMapping("/buy")
    public void orderProcess(@RequestBody OrderForm form, HttpSession session){
        CartDTO dto = new CartDTO();

        for(int i = 0 ; i < form.getId().size(); i++) {
            Optional<Product> product = productRepository.findById(form.getId().get(i));
            dto.setAmount(form.getAmount().get(i));
            dto.setProdPrice(product.get().getPrice());
            dto.setProdName(product.get().getPname());
            dto.setProdId(product.get().getId());
        }
        session.setAttribute("form", dto);
        session.setAttribute("totalPrice", dto.getAmount() * dto.getProdPrice());
    }

    @Transactional
    @PostMapping("/addOrders")
    public void addOrders(@RequestBody OrderForm form, HttpSession session){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Order order = new Order();
        String orderId = "O"+System.currentTimeMillis();
        List<Long> id = form.getId();
        order.setOId(orderId);
        order.setTotalPrice(form.getTotalPrice());
        order.setStatus("주문요청");
        order.setUid(auth.getName());
        List<OrderProduct> lists = new ArrayList<>();
        Optional<Product> product;
        for(int i = 0 ; i < id.size(); i++){
            product = productRepository.findById(form.getId().get(i));
            product.get().setStock(product.get().getStock() - form.getAmount().get(i));
            OrderProduct oprud = new OrderProduct();
            oprud.setAmount(form.getAmount().get(i));
            oprud.setProdId(form.getId().get(i));
            oprud.setOrder(order);
            lists.add(i, oprud);
        }
        order.setProducts(lists);
        orderRepository.save(order);
        if(cartRepository.findByUid(auth.getName()).size() > 0){
            for(int i = 0 ; i < id.size(); i++){
                Cart cart = cartRepository.findByProdId(form.getId().get(i));
                cartRepository.deleteById(cart.getId());
                session.removeAttribute("form");
                session.removeAttribute("totalprice");
            }
        }
    }

    @GetMapping
    public List<OrderDTO> getOrder(){
        String role="member";
        return getList(role);
    }

    @GetMapping("/admin")
    public List<OrderDTO> getOrders(@PageableDefault(sort = {"id"}, size = 1, direction = Sort.Direction.DESC) Pageable pageable){
        return getList("admin");
    }

    @PutMapping
    public void applyCancel(@RequestBody OrderDTO orderDTO){
        Optional<Order> order = orderRepository.findById(orderDTO.getId());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(order.get().getUid().equals(auth.getName())){
                order.get().setStatus("취소요청");
                orderRepository.save(order.get());
        }else{
            return;
        }
    }
    @PutMapping("/admin")
    public void applyStatus(@RequestBody OrderDTO orderDTO){
        Optional<Order> order = orderRepository.findById(orderDTO.getId());
        System.out.println("ddd "+order.get().getStatus());
        if(order.get().getStatus().equals("주문요청")){
            order.get().setStatus("주문완료");
        }else if(order.get().getStatus().equals("취소요청")){
            order.get().setStatus("취소완료");
        }
        orderRepository.save(order.get());
    }

    @GetMapping("/{id}")
    public boolean isStatus(@PathVariable Long id){
        Optional<Order> order = orderRepository.findById(id);
        if(order.get().getStatus().equals("주문요청"))  return true;
        else return false;
    }

    public List<OrderDTO> getList(String role){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Order> order = orderRepository.findByUid(auth.getName());

        if(role.equals("admin")){
            order = (List<Order>) orderRepository.findAll();
        }

        List<OrderDTO> list = new ArrayList<>();
        for(int i = 0 ; i < order.size(); i++){
            OrderDTO dto = new OrderDTO();
            List<ProductDTO> list1 = new ArrayList<>();
            for(int j = 0 ; j < order.get(i).getProducts().size(); j++){
                Optional<Product> product = productRepository.findById(order.get(i).getProducts().get(j).getProdId());
                ProductDTO productDTO = new ProductDTO();
                productDTO.setProdId(product.get().getId());
                productDTO.setProdName(product.get().getPname());
                productDTO.setPrice(product.get().getPrice());
                productDTO.setAmount(order.get(i).getProducts().get(j).getAmount());
                list1.add(j, productDTO);
            }
            dto.setProducts(list1);
            dto.setTotalPrice(order.get(i).getTotalPrice());
            dto.setOrderId(order.get(i).getOId());
            dto.setId(order.get(i).getId());
            dto.setUid(order.get(i).getUid());
            dto.setStatus(order.get(i).getStatus());
            list.add(i, dto);
        }
        return list;
    }
}

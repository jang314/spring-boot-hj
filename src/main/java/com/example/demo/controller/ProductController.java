package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.model.ProductForm;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Optional;


@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<?> addProduct(@ModelAttribute("upload") ProductForm form) {
        if(form.getFile().isEmpty())  return new ResponseEntity<>(HttpStatus.OK);

        try{
            Product product = new Product();
            File uploadFile = null;
            String path = "C:\\Users\\Lenovo\\IdeaProjects\\demo\\src\\main\\resources\\static\\images";
            uploadFile=new File(path, System.currentTimeMillis()+"-"+form.getFile().getOriginalFilename());
            product.setPname(form.getPname());
            product.setContent(form.getContent());
            product.setPrice(form.getPrice());
            product.setStock(form.getStock());
            form.getFile().transferTo(uploadFile);
            product.setFile(uploadFile.getName());
            productRepository.save(product);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("success" + form.getFile().getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping
    public Page<Product> getList(@PageableDefault(sort = {"id"}, size = 10, direction = Sort.Direction.DESC) Pageable pageable){
        return  productRepository.findAll(pageable);
    }

    @GetMapping(value = "{id}")
    public Optional<Product> getView(@PathVariable("id") Long id){
        return productRepository.findById(id);
    }

    @PutMapping
    public ResponseEntity<?> updateProduct(@ModelAttribute("update") ProductForm productForm) {
        System.out.println(productForm.toString());

        try{
            File uploadFile = null;
            String path = "C:\\Users\\Lenovo\\IdeaProjects\\demo\\src\\main\\resources\\static\\images";
            uploadFile=new File(path, System.currentTimeMillis()+"-"+productForm.getFile().getOriginalFilename());
            productForm.getFile().transferTo(uploadFile);
            Optional<Product> product = productRepository.findById(productForm.getId());
            File deleteFile = new File(path+"/"+product.get().getFile());

            if(productForm.getFile().isEmpty()){
                System.out.println("333");
                product.get().setFile(product.get().getFile());
//                return new ResponseEntity<>(HttpStatus.OK);
            }else{
                product.get().setFile(uploadFile.getName());
                if(deleteFile.exists()) deleteFile.delete();
            }

//            if(deleteFile.exists()){
//                deleteFile.delete();
//            }

            product.get().setPname(productForm.getPname());
            product.get().setPrice(productForm.getPrice());
            product.get().setContent(productForm.getContent());
            System.out.println("222 : " + product.get().toString());
            productRepository.save(product.get());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("success" + productForm.getFile().getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping
    public void outMember(@RequestBody Product product){
        if(product != null){
            String path = "C:\\Users\\Lenovo\\IdeaProjects\\demo\\src\\main\\resources\\static\\images";
            File file = new File("C:\\Users\\Lenovo\\IdeaProjects\\demo\\src\\main\\resources\\static\\images/"+productRepository.findById(product.getId()).get().getFile());
            if(file.exists()){
                file.delete();
                productRepository.deleteById(productRepository.findById(product.getId()).get().getId());
            }
        }else{
        }
    }

}

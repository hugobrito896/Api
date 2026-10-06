package com.index1.crud.controllers;

import com.index1.crud.dtos.ProductDto;
import com.index1.crud.model.Product;
import com.index1.crud.repositories.ProductRepository;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/products")


public class ProductController {

    @Autowired
    ProductRepository repository;

    @GetMapping
    public ResponseEntity getAll() {
        List<Product> listProducts = repository.findAll(Sort.by("id"));

        return ResponseEntity.status(HttpStatus.OK).body(listProducts);
    }

    @PostMapping
    public ResponseEntity save(@RequestBody ProductDto dto) {

        var product = new Product();
        BeanUtils.copyProperties(dto, product);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(product));

    }

    @GetMapping("/{id}")
    public ResponseEntity getByid(@PathVariable(value = "id") Integer id) {

        Optional product = repository.findById(id);

        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("Product not found"));

        } else {
            return ResponseEntity.status(HttpStatus.FOUND).body(product.get());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") Integer id) {

        Optional<Product> product = repository.findById(id);

        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        } else {
            repository.delete(product.get());
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted");
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity upadate(@PathVariable(value = "id") Integer id,@RequestBody ProductDto dto) {

        Optional<Product> product = repository.findById(id);

        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        var productModel = product.get();
        BeanUtils.copyProperties(dto, productModel);
        var saved = repository.save(productModel);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "message", "Produto atualizado com sucesso!",
                "product", saved
        ));
    }


}

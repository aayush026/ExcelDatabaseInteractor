package com.ayushjoshi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ayushjoshi.model.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer>{
     
}

package com.moviezon.product.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    public List<Product> findAll();

    public List<Product> findByTitleContaining(String searchTerm);

    public Product findByProductId(Integer productId);
}

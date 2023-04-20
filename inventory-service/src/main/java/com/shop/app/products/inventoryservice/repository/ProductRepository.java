package com.shop.app.products.inventoryservice.repository;

import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.shop.app.products.inventoryservice.dto.ProductDto;
import com.shop.app.products.inventoryservice.entity.Product;

import reactor.core.publisher.Flux;
@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String>{

	

	Flux<ProductDto> findByPriceBetween(Range<Double> priceRange);

}

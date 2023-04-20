package com.shop.app.products.inventoryservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;

import com.shop.app.products.inventoryservice.dto.ProductDto;
import com.shop.app.products.inventoryservice.repository.ProductRepository;
import com.shop.app.products.inventoryservice.utils.AppUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {
	
	private final ProductRepository productRepository;

	@Autowired
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	public Flux<ProductDto> getProducts()
	{
		return productRepository.findAll().map(AppUtils::entityToDTO);
	}
	public Mono<ProductDto> getProductById(String id)
	{
		return productRepository.findById(id).map(AppUtils::entityToDTO);
	}
	
	public Flux<ProductDto> getProductInRange(Double min,Double max)
	{
		return productRepository.findByPriceBetween(Range.closed(min, max));
	}
	
	public Mono<ProductDto> createProduct(Mono<ProductDto> productDtoMono)
	{
		return productDtoMono.map(AppUtils::dtoToEntity)
		.flatMap(productRepository::insert)
		.map(AppUtils::entityToDTO);
	}
	public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono,String id)
	{
		
		return productRepository.findById(id) //record from db
				.flatMap(p->productDtoMono.map(AppUtils::dtoToEntity) //convert to entity
						.doOnNext(e->e.setId(id))) //update id
						.flatMap(productRepository::save)
						.map(AppUtils::entityToDTO);
	}
	
	public Mono<Void> deleteProductById(String id)
	{
		return productRepository.deleteById(id);
	}
}

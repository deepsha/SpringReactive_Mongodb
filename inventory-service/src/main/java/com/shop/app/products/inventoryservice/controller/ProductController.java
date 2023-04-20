package com.shop.app.products.inventoryservice.controller;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.shop.app.products.inventoryservice.dto.ProductDto;
import com.shop.app.products.inventoryservice.exception.ProductAPIexception;
import com.shop.app.products.inventoryservice.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/inventory")
public class ProductController {
	
	private final Logger logger=Logger.getLogger("ProductController");
	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping("/products")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Flux<ProductDto>> getProducts()
	{
		Flux<ProductDto> products= productService.getProducts();
		logger.info("getProducts:"+products.toString());
		HttpStatus status = (products != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<>(products, status);
	}
	@GetMapping("/products/{id}")
	@ResponseStatus(HttpStatus.OK)
	public  ResponseEntity<Mono<ProductDto>> getProductById(@PathVariable String id)
	{
		Mono<ProductDto> product = productService.getProducts()
		.filter(book -> book.getId().equals(id))
        .next()
        .switchIfEmpty(Mono.error(new ProductAPIexception("Product not found with id " + id)));
		
		HttpStatus status = (product != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<>(product, status);
	}
	@GetMapping("/products/range")
	@ResponseStatus(HttpStatus.OK)
	public  ResponseEntity<Flux<ProductDto>> getProductsInRange(@RequestParam("min") Double min, @RequestParam("max") Double max)
	{
		Flux<ProductDto> product= productService.getProductInRange(min,max);
		HttpStatus status = (product != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<>(product, status);
	}
	@PostMapping("/products")
	@ResponseStatus(HttpStatus.OK)
	public  ResponseEntity<Mono<ProductDto>> createProducts(@RequestBody Mono<ProductDto> productDto)
	{
		logger.info("createProducts:"+productDto.hasElement());
		Mono<ProductDto> product= productService.createProduct(productDto);
		HttpStatus status = (product != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<>(product, status);
	}
	@PutMapping("/products/{id}")
	public  ResponseEntity<Mono<ProductDto>> updateProducts(@RequestBody Mono<ProductDto> productDto,@PathVariable String id)
	{
		logger.info("updateProducts:"+productDto);
		Mono<ProductDto> product= productService.updateProduct(productDto,id);
		HttpStatus status = (product != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<>(product, status);
	}
	@DeleteMapping("/products/{id}")
	
	public  ResponseEntity<Mono<Void>> deleteProducts(@PathVariable String id)
	{
		Mono<Void> product= productService.deleteProductById(id);
		HttpStatus status = (product != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<>(product, status);
	}
}

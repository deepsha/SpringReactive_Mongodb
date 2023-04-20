package com.shop.app.products.inventoryservice;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.shop.app.products.inventoryservice.controller.ProductController;
import com.shop.app.products.inventoryservice.dto.ProductDto;
import com.shop.app.products.inventoryservice.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import  static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class InventoryServiceApplicationTests {

	private static final String URL="http://localhost:8082/inventory";
	@Autowired
	private WebTestClient webTestClient;
	
	@MockBean
	private ProductService productService;
	@Test
	public void contextLoads() {
		Assertions.assertNotNull(productService);
		Assertions.assertNotNull(webTestClient);
	}
	 @Test
	    public void addProductTest(){
			Mono<ProductDto> productDtoMono=Mono.just(new ProductDto("1","Pencils",10,3.0));
			when(productService.createProduct(productDtoMono)).thenReturn(productDtoMono);

			webTestClient.post().uri(URL+"/products")
					.body(Mono.just(productDtoMono),ProductDto.class)
					.exchange()
					.expectStatus().isOk();//200

		}


		@Test
		public void getProductsTest(){
			Flux<ProductDto> productDtoFlux=Flux.just(new ProductDto("102","mobile",1,10.0),
					new ProductDto("103","TV",1,50000));
			when(productService.getProducts()).thenReturn(productDtoFlux);

			Flux<ProductDto> responseBody = webTestClient.get().uri(URL+"/products")
					.exchange()
					.expectStatus().isOk()
					.returnResult(ProductDto.class)
					.getResponseBody();

			StepVerifier.create(responseBody)
					.expectSubscription()
					.expectNext(new ProductDto("102","mobile",1,100.0))
					.expectNext(new ProductDto("103","TV",1,500.0))
					.verifyComplete();

		}


		@Test
		public void getProductTest(){
			Mono<ProductDto> productDtoMono=Mono.just(new ProductDto("102","mobile",1,10000));
			when(productService.getProductById("102")).thenReturn(productDtoMono);

			Flux<ProductDto> responseBody = webTestClient.get().uri(URL+"/products/102")
					.exchange()
					.expectStatus().isOk()
					.returnResult(ProductDto.class)
					.getResponseBody();

			StepVerifier.create(responseBody)
					.expectSubscription()
					.expectNextMatches(p->p.getName().equals("mobile"))
					.verifyComplete();
		}


		@Test
		public void updateProductTest(){
			Mono<ProductDto> productDtoMono=Mono.just(new ProductDto("102","mobile",1,100.0));
			when(productService.updateProduct(productDtoMono,"102")).thenReturn(productDtoMono);

			webTestClient.put().uri(URL+"/products/update/102")
					.body(Mono.just(productDtoMono),ProductDto.class)
					.exchange()
					.expectStatus().isOk();//200
		}

		@Test
		public void deleteProductTest(){
	    	given(productService.deleteProductById(any())).willReturn(Mono.empty());
			webTestClient.delete().uri(URL+"/products/delete/102")
					.exchange()
					.expectStatus().isOk();//200
		}



}

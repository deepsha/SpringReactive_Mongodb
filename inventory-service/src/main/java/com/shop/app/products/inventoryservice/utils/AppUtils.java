package com.shop.app.products.inventoryservice.utils;

import org.springframework.beans.BeanUtils;

import com.shop.app.products.inventoryservice.dto.ProductDto;
import com.shop.app.products.inventoryservice.entity.Product;


public class AppUtils {
	
	public static ProductDto entityToDTO(Product product)
	{
		ProductDto productDTO=new ProductDto();
		BeanUtils.copyProperties(product, productDTO);
		return productDTO;
	}
	public static Product dtoToEntity(ProductDto productDTO)
	{
		Product product=new Product();
		BeanUtils.copyProperties(productDTO, product);
		return product;
	}

}

package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.helper.PageableResponse;

public interface ProductService {
//	create
	ProductDto create(ProductDto productDto);
//	update
	ProductDto update(ProductDto productDto,String productId);
//	delete
	void delete(String productId);
//	getAllProduct
	PageableResponse<ProductDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);
//	getSingleProductById
	ProductDto getSingleProductById(String productId);
//	getAllProduct : Those are LIVE
	PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize,String sortBy,String sortDir);
//	search Product
	PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber,int pageSize,String sortBy,String sortDir);
//	Create Product with category
	ProductDto createProductWithCategory(ProductDto productDto,String categoryId);
//	Update category of Product
	ProductDto updateCategory(String productId,String categoryId);
//	getAllProductsOfParticularCategory
	PageableResponse<ProductDto> getAllProductsOfParticularCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir);
	
	
}

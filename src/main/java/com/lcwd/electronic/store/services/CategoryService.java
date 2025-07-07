package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.helper.PageableResponse;

public interface CategoryService {

//	create
	CategoryDto create(CategoryDto categoryDto);

//	update
	CategoryDto update(CategoryDto categoryDto, String categoryId);

//	getAll
	PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);

//	getSingleCategory
	CategoryDto getSingleCategoryById(String categoryId);

//	delete
	void delete(String categoryId);

}

package com.lcwd.electronic.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.helper.ApiResponseMessage;
import com.lcwd.electronic.store.helper.PageableResponse;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.ProductService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

//	create
//	http://localhost:8080/categories
	@PostMapping
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
		CategoryDto createdCategory = this.categoryService.create(categoryDto);
		return new ResponseEntity<CategoryDto>(createdCategory, HttpStatus.CREATED);
	}

//  update
//	http://localhost:8080/categories/{ID}
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> updatedCategory(@Valid @RequestBody CategoryDto categoryDto,
			@PathVariable("categoryId") String categoryId) {
		CategoryDto updatedCategory = this.categoryService.update(categoryDto, categoryId);
		return new ResponseEntity<CategoryDto>(updatedCategory, HttpStatus.OK);
	}

//	delete
//	http://localhost:8080/categories/{ID}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable("categoryId") String categoryId) {
		this.categoryService.delete(categoryId);

		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
				.message("Category is Deleted SuccessFully !!").success(true).status(HttpStatus.OK).build();

		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);

	}

//	getAllCategory
//	http://localhost:8080/categories
	@GetMapping
	public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "50", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortby,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

		PageableResponse<CategoryDto> categoryDtos = this.categoryService.getAll(pageNumber, pageSize, sortby, sortDir);
		return new ResponseEntity<PageableResponse<CategoryDto>>(categoryDtos, HttpStatus.OK);

	}

//	getSingleCategory
//	http://localhost:8080/categories/{categoryId}
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable("categoryId") String categoryId) {
		CategoryDto categoryDto = this.categoryService.getSingleCategoryById(categoryId);
		return new ResponseEntity<CategoryDto>(categoryDto, HttpStatus.OK);
	}

//	ASSIGNMENT:-1
//	create file Upload api
//	create serve file api
//	when we delete the category then it should be delete category image...

//	create Product with category
//	http://localhost:8080/categories/{categoryId}/products
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{categoryId}/products")
	public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable String categoryId,
			@RequestBody ProductDto productDto) {
		ProductDto productWithCategory = this.productService.createProductWithCategory(productDto, categoryId);
		return new ResponseEntity<ProductDto>(productWithCategory, HttpStatus.CREATED);

	}

//	update category of Product
//	http://localhost:8080/categories/{categoryId}/products/{productId}
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{categoryId}/products/{productId}")
	public ResponseEntity<ProductDto> updateCategoryOfProduct(@PathVariable String categoryId,
			@PathVariable String productId) {
		ProductDto updatedCategory = this.productService.updateCategory(productId, categoryId);
		return new ResponseEntity<ProductDto>(updatedCategory, HttpStatus.OK);
	}

//	fetch the all products of Particular category
//  Returns all Products of given category
//	http://localhost:8080/categories/allproducts/{categoryId}
	@GetMapping("/allproducts/{categoryId}")
	public ResponseEntity<PageableResponse<ProductDto>> getAllProductsByCategory(@PathVariable String categoryId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "50", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortby,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

		PageableResponse<ProductDto> allProductsOfParticularCategory = this.productService
				.getAllProductsOfParticularCategory(categoryId, pageNumber, pageSize, sortby, sortDir);
		return new ResponseEntity<PageableResponse<ProductDto>>(allProductsOfParticularCategory, HttpStatus.OK);

	}

}

package com.lcwd.electronic.store.services.Impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.helper.PageableResponse;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Value("${product.image.path}")
	private String imagePath;

	private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
	
//	create
	@Override
	public ProductDto create(ProductDto productDto) {

		String productId = UUID.randomUUID().toString();

		Product product = this.mapper.map(productDto, Product.class);
		product.setProductId(productId);
		product.setAddedDate(new Date());

		Product savedProduct = this.productRepository.save(product);

		ProductDto savedProductDto = mapper.map(savedProduct, ProductDto.class);

		return savedProductDto;
	}

//	update
	@Override
	public ProductDto update(ProductDto productDto, String productId) {

//		fetch the product of given Id
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found of given Id..!!"));

		product.setTitle(productDto.getTitle());
		product.setDescription(productDto.getDescription());
		product.setPrice(productDto.getPrice());
		product.setDiscountedPrice(productDto.getDiscountedPrice());
		product.setLive(productDto.isLive());
		// product.setAddedDate(productDto.getAddedDate());
		product.setQuantity(productDto.getQuantity());
		product.setStock(productDto.isStock());
		product.setProductImageName(productDto.getProductImageName());

		Product updatedProduct = this.productRepository.save(product);

		ProductDto updateProductDto = this.mapper.map(updatedProduct, ProductDto.class);
		return updateProductDto;
	}

//	delete
	@Override
	public void delete(String productId) {

		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found of given Id..!!"));

//		delete  product profile image 
		String imageName = product.getProductImageName();
//		EX:- images/products/abc144245dfd5fd57-fd21df5.png
		String fullPath = imagePath + imageName;
		try {
			Path path = Paths.get(fullPath);
			Files.delete(path);
		} catch (NoSuchFileException ex) {
			log.info("Product image not found in folder..!!");
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.productRepository.delete(product);

	}

//	getAllProduct
	@Override
	public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {

		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Product> findAll = this.productRepository.findAll(pageable);

		PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(findAll, ProductDto.class);

		return pageableResponse;
	}

//	getSingleProductById
	@Override
	public ProductDto getSingleProductById(String productId) {
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found of given Id..!!"));
		return mapper.map(product, ProductDto.class);
	}

//	getAllLive
	@Override
	public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {

		Sort sort = (sortBy.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Product> page = this.productRepository.findAll(pageable);

		PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);

		return pageableResponse;
	}

//	searchByTitle
	@Override
	public PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy,
			String sortDir) {

		Sort sort = null;

		if (sortBy.equalsIgnoreCase("desc"))
			sort = Sort.by(sortBy).descending();
		else
			sort = Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Product> page = this.productRepository.findByTitleContaining(subTitle, pageable);

		PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);

		return pageableResponse;
	}

//	create product with category
	@Override
	public ProductDto createProductWithCategory(ProductDto productDto, String categoryId) {

//		fetch the category from database
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found of given Id..!!"));

		Product product = this.mapper.map(productDto, Product.class);

		String productId = UUID.randomUUID().toString();
		product.setProductId(productId);
		product.setAddedDate(new Date());
//		set the category in product 
		product.setCategory(category);

		Product savedProduct = this.productRepository.save(product);

		ProductDto savedProductDto = mapper.map(savedProduct, ProductDto.class);

		return savedProductDto;
	}

//	update category of Product
	@Override
	public ProductDto updateCategory(String productId, String categoryId) {

//		fetch the Product from database
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found of given Id..!!"));

//		fetch the category from database
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found of given Id..!!"));
//		set the category
		product.setCategory(category);
//		save the product it means setted category will be save...
		Product savedProduct = this.productRepository.save(product);
		 
		return mapper.map(savedProduct,ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAllProductsOfParticularCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir)
	{
		//fetch the category from database
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found of given Id..!!"));
		
		Sort sort = null;

		if (sortBy.equalsIgnoreCase("desc"))
			sort = Sort.by(sortBy).descending();
		else
			sort = Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<Product> page = this.productRepository.findByCategory(category,pageable);
		
		PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page,ProductDto.class);
		
		return pageableResponse;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

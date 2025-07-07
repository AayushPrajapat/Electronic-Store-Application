package com.lcwd.electronic.store.services.Impl;

import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.helper.PageableResponse;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public CategoryDto create(CategoryDto categoryDto) {

		String categoryId = UUID.randomUUID().toString();
		categoryDto.setCategoryId(categoryId);
		
		Category category = mapper.map(categoryDto, Category.class);
	
		Category savedCategory = this.categoryRepository.save(category);

		CategoryDto categoryDto2 = mapper.map(savedCategory, CategoryDto.class);

		return categoryDto2;
	}

	@Override
	public CategoryDto update(CategoryDto categoryDto, String categoryId) {

//		get Category of given Id
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not Found With Given Id " + categoryId));
//		update category details
		category.setTitle(categoryDto.getTitle());
		category.setDescription(categoryDto.getDescription());
		category.setCoverImage(categoryDto.getCoverImage());

		Category updatedCategory = this.categoryRepository.save(category);
		
		CategoryDto categoryDto2 = mapper.map(updatedCategory, CategoryDto.class);

		return categoryDto2;
	}

	@Override
	public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {

		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Category> page = this.categoryRepository.findAll(pageable);

		PageableResponse<CategoryDto> categoryDtos = Helper.getPageableResponse(page, CategoryDto.class);

		return categoryDtos;
	}

	@Override
	public CategoryDto getSingleCategoryById(String categoryId) {
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not Found With Given Id " + categoryId));

		CategoryDto categoryDto = mapper.map(category, CategoryDto.class);

		return categoryDto;
	}

	@Override
	public void delete(String categoryId) {
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not Found With Given Id " + categoryId));
		
		this.categoryRepository.delete(category);

	}

}

package com.lcwd.electronic.store.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;

public interface ProductRepository extends JpaRepository<Product, String> {

	Page<Product> findByTitleContaining(String title, Pageable pageable);

	List<Product> findByIsLiveTrue(Pageable pageable);
	
//	Returns all the Products of given category
	Page<Product> findByCategory(Category category,Pageable pageable);

}

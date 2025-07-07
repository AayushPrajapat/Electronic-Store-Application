package com.lcwd.electronic.store.controllers;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.helper.ApiResponseMessage;
import com.lcwd.electronic.store.helper.ImageResponse;
import com.lcwd.electronic.store.helper.PageableResponse;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private FileService fileService;

	@Value("${product.image.path}")
	private String imagePath;

//	create
//	http://localhost:8080/products
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
		ProductDto created = this.productService.create(productDto);
		return new ResponseEntity<ProductDto>(created, HttpStatus.CREATED);
	}

//	update
//	http://localhost:8080/products/{productId}
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{productId}")
	public ResponseEntity<ProductDto> updateProduct(@PathVariable String productId,
			@RequestBody ProductDto productDto) {
		ProductDto updatedProduct = this.productService.update(productDto, productId);
		return new ResponseEntity<ProductDto>(updatedProduct, HttpStatus.OK);
	}

//	delete
//	http://localhost:8080/products/{productId}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{productId}")
	public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId) {
		this.productService.delete(productId);
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder().message("Product SuccessFully Deleted..!!")
				.status(HttpStatus.OK).success(true).build();
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);
	}

//	getSingleProduct
//	http://localhost:8080/products/{productId}
	@GetMapping("/{productId}")
	public ResponseEntity<ProductDto> getSingleProduct(@PathVariable String productId) {
		ProductDto productDto = this.productService.getSingleProductById(productId);
		return new ResponseEntity<ProductDto>(productDto, HttpStatus.OK);

	}

//	getAllProducts
//	http://localhost:8080/products
	@GetMapping
	public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "50", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

		PageableResponse<ProductDto> productDtos = this.productService.getAll(pageNumber, pageSize, sortBy, sortDir);

		return new ResponseEntity<PageableResponse<ProductDto>>(productDtos, HttpStatus.OK);
	}

//	getAllLive
	// http://localhost:8080/products/live
	@GetMapping("/live")
	public ResponseEntity<PageableResponse<ProductDto>> getAllLive(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "50", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

		PageableResponse<ProductDto> allLive = this.productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<ProductDto>>(allLive, HttpStatus.OK);
	}

//	getsearch
//  http://localhost:8080/products/search/{keyword}
	@GetMapping("/search/{keyword}")
	public ResponseEntity<PageableResponse<ProductDto>> getAllSearch(@PathVariable String keyword,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "50", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

		PageableResponse<ProductDto> searchByTitle = this.productService.searchByTitle(keyword, pageNumber, pageSize,
				sortBy, sortDir);

		return new ResponseEntity<PageableResponse<ProductDto>>(searchByTitle, HttpStatus.OK);

	}

//	upload Image
//	http://localhost:8080/products/image/{productId}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/image/{productId}")
	public ResponseEntity<ImageResponse> uploadProductImage(@PathVariable String productId,
			@RequestParam("productImage") MultipartFile image) throws IOException {

		String fileName = fileService.uplaodImage(image, imagePath);

		ProductDto productDto = this.productService.getSingleProductById(productId);

		productDto.setProductImageName(fileName);
		
		ProductDto updatedProductDto = productService.update(productDto, productId);

		ImageResponse imageResponse = ImageResponse.builder().imageName(updatedProductDto.getProductImageName())
				.message("Product Image is SuccessFully Uploaded..!!").status(HttpStatus.OK).success(true).build();

		return new ResponseEntity<ImageResponse>(imageResponse, HttpStatus.CREATED);

	}
	
//	served Image
//	http://localhost:8080/products/image/{productId}
	@GetMapping("/image/{productId}")
	public void serveUserImage(@PathVariable String productId,HttpServletResponse response) throws IOException {
		ProductDto productDto = this.productService.getSingleProductById(productId);
		InputStream resource = fileService.getResource(imagePath, productDto.getProductImageName());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		int copy = StreamUtils.copy(resource,response.getOutputStream());
		System.out.println("served image"+copy);
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

package com.lcwd.electronic.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.helper.ApiResponseMessage;
import com.lcwd.electronic.store.services.CartService;

@RestController
@RequestMapping("carts")
public class CartController {

	@Autowired
	private CartService cartService;

//	addItemToCart
//	http://localhost:8080/carts/{userId}
	@PostMapping("/{userId}")
	public ResponseEntity<CartDto> addItemToCart(@RequestBody AddItemToCartRequest request,
			@PathVariable String userId) {
		CartDto cartDto = this.cartService.addItemToCart(userId, request);
		return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
	}

//	removeItem from cart
//	http://localhost:8080/carts/{userId}/items/{itemId}
	@PutMapping("/{userId}/items/{itemId}")
	public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId,
			@PathVariable int itemId) {
		this.cartService.removeItemFromCart(userId, itemId);
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder().message("Item is Removed..!!")
				.status(HttpStatus.OK).success(true).build();
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);
	}

//  Remove all items from cart
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId) {
		this.cartService.clearCart(userId);
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder().message("now,Cart is blank..!!")
				.status(HttpStatus.OK).success(true).build();
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);
	}

//	getCart of Particular User...
//	http://localhost:8080/carts/{userId}
	@GetMapping("/{userId}")	
	public ResponseEntity<CartDto> getParticularCartByUser(@PathVariable String userId) {
		CartDto cartDto = this.cartService.getCartByUser(userId);
		return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
	}

}

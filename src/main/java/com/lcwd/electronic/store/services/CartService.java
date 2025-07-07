package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;

public interface CartService {
//	Add Items to Cart
//  Case1:	cart for user is not avilable: we will create the cart and then add the items
//	Case2:  cart avilable then add the items to cart
//	Case3:  if items already avilable then we will increase the quantity of item 
	CartDto addItemToCart(String userId, AddItemToCartRequest request);

//	Remove item from cart:
	void removeItemFromCart(String userId, int cartItem);

// Remove all items from cart
	void clearCart(String userId);
//	Particular user ka cart get krna 
	CartDto getCartByUser(String userId);

}

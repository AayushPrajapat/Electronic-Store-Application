package com.lcwd.electronic.store.services.Impl;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.BadApiRequest;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CartItemRepository;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.CartService;

//		iss vali class ko formate code nhi krna hai

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private ModelMapper mapper;
	
	

	@Override
	public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

		int quantity = request.getQuantity();
		String productId = request.getProductId();
		
		if (quantity<=0) {
			throw new BadApiRequest("Requested Quantity is not Valid..!!");
		}

//		fetch the Product
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found in DataBase..!!"));
//		fetch the user from db
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found in database..!!"));
		
//		fetch the cart through user
		Cart cart = null;
		try {
//			if cart is avilable in database 
			cart = this.cartRepository.findByUser(user).get();

		} catch (NoSuchElementException e) {
//			if cart is not avilable in database..then we will create the cart
			cart = new Cart();
			cart.setCartId(UUID.randomUUID().toString());
			cart.setCreatedAt(new Date());
		}

//		Perform Cart Operations
//		 if cart items already Present; then Update
		AtomicReference<Boolean> updated = new AtomicReference<>(false); 
		List<CartItem> items2 = cart.getItems();
		
		items2 = items2.stream().map(item->{
			if (item.getProduct().getProductId().equals(productId)) {
//				item already present in cart
				item.setQuantity(quantity);
				item.setTotalPrice(quantity*product.getDiscountedPrice());
				updated.set(true);
			}
			return item;
		}).collect(Collectors.toList());
		
		//cart.setItems(updatedItems);
		
//		Create items
		if (!updated.get()) {
			
		CartItem cartItem = CartItem.builder().quantity(quantity).totalPrice(quantity * product.getDiscountedPrice()).cart(cart)
				.product(product).build();

		
		
		/*
			* get the items from cart 
* --> agr naya cart hai toh items variable blank milega...
* --> agr purana cart hai toh items ki puri list milegi...fir ushi mai add kr dege items
*/
		List<CartItem> items = cart.getItems();
		items.add(cartItem);
//										OR
		//cart.getItems().add(cartItem); There are two ways to write the lines (above)
		
		}

		cart.setUser(user);
		Cart updatedCart = this.cartRepository.save(cart);

		return mapper.map(updatedCart, CartDto.class);
	}

	@Override
	public void removeItemFromCart(String userId, int cartItemId) {
//			agr hum particular item ko remove krna chahte hai from cart se toh cartItemId pass krna hoga...
		
//		conditions
		
		
		CartItem cartItem = this.cartItemRepository.findById(cartItemId).orElseThrow(()-> new ResourceNotFoundException("CartItem not found in DataBase..!!"));
		
		cartItemRepository.delete(cartItem);
		
		
		
		
	}

	@Override
	public void clearCart(String userId) {
		
//		fetch the user from db
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found in database..!!"));

		Cart cart = this.cartRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("Cart of given user not found..!!"));
		cart.getItems().clear();
		
		Cart cleardCartWithSave = this.cartRepository.save(cart);
		
		System.out.println(cleardCartWithSave);
		
	}

	@Override
	public CartDto getCartByUser(String userId) {
		
		
//		fetch the user from db
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found in database..!!"));

		Cart cart = this.cartRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("Cart of given user not found..!!"));

		return mapper.map(cart,CartDto.class);
	}

	
	
}

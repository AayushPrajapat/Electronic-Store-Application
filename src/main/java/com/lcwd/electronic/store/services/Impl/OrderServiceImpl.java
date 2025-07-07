package com.lcwd.electronic.store.services.Impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Order;
import com.lcwd.electronic.store.entities.OrderItem;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.BadApiRequest;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.helper.PageableResponse;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.OrderItemRepository;
import com.lcwd.electronic.store.repositories.OrderRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public OrderDto createOrder(CreateOrderRequest orderDto) {
		
		
		String userId = orderDto.getUserId();
		String cartId = orderDto.getCartId();
		
//		fetch user
		User user = this.userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not Found with given Id"+userId));
//		fetch cart
		Cart cart = this.cartRepository.findById(cartId).orElseThrow(()-> new ResourceNotFoundException("Cart not Found with given Id"+cartId));
		
//		iss cart mai jitne bhi items hai ushe orders mai convert krna hai
//		cartItem ko convert krke orderItem mai orr firr order mai add krna 
		
		List<CartItem> cartItems = cart.getItems();
		
		if (cartItems.size()<=0) {
			throw new BadApiRequest("Invalid Number of Items in Cart..!!");
		}
//		create Order
		
		Order order = new Order();
		
		order.setOrderId(UUID.randomUUID().toString());
		order.setBillingName(orderDto.getBillingName());
		order.setBillingAddress(orderDto.getBillingAddress());
		order.setBillingPhone(orderDto.getBillingPhone());
		order.setOrderedDate(new Date());
		order.setDeliveredDate(null);
		order.setPaymentStatus(orderDto.getPaymentStatus());
		order.setOrderStatus(orderDto.getOrderStatus());
		order.setUser(user);
		
		
		AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
		
//		sare cartItems ko convert krege orderItems mai
		List<OrderItem> orderItems = cartItems.stream().map(cartItem->{
//			cartItem-->orderItem
			OrderItem orderItem = OrderItem.builder()
			.quantity(cartItem.getQuantity())
			.product(cartItem.getProduct())
			.totalPrice(cartItem.getQuantity()*cartItem.getProduct().getDiscountedPrice())
			.order(order)
			.build();
			orderAmount.set(orderAmount.get()+orderItem.getTotalPrice());
			
			return orderItem;
			
		}).collect(Collectors.toList());
		
		order.setOrderItems(orderItems);
		order.setOrderAmount(orderAmount.get());
		
//		clear the cart
		cart.getItems().clear();
		
		cartRepository.save(cart);
		Order savedOrder = orderRepository.save(order);

		return mapper.map(savedOrder,OrderDto.class) ;
	}

	@Override
	public void removeOrder(String orderId) {
		
		Order order = this.orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order not found with Given Id"+orderId));
		this.orderRepository.delete(order);
//		agr order delete honge toh orderItems ke bhi delete hongee kuki Order Table mai Cascade.ALL laga rakha hai 
//		jab remove honge toh uske child bhi remove honge
	}

	@Override
	public List<OrderDto> getOrdersOfUser(String userId) {
		
//		fetch user
		User user = this.userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not Found with given Id"+userId));

		List<Order> orders = this.orderRepository.findByUser(user);
		List<OrderDto> orderDtos = orders.stream().map(order->mapper.map(order,OrderDto.class)).collect(Collectors.toList());
		
		return orderDtos;
	}

	@Override
	public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
		
		Sort sort = null;
		if (sortDir.equalsIgnoreCase("desc")) {
			sort = Sort.by(sortBy).descending();
		}else {
			sort =Sort.by(sortBy).ascending();
		}
				
    	Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
    	
    	Page<Order> page = orderRepository.findAll(pageable);
    	
    	PageableResponse<OrderDto> pageableResponse = Helper.getPageableResponse(page,OrderDto.class);
    	
    	return pageableResponse;
	}

}

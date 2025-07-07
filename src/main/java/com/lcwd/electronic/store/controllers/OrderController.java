package com.lcwd.electronic.store.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.helper.ApiResponseMessage;
import com.lcwd.electronic.store.helper.PageableResponse;
import com.lcwd.electronic.store.services.OrderService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

//	create order
	@PostMapping
	public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
		OrderDto createdOrderDto = this.orderService.createOrder(createOrderRequest);
		return new ResponseEntity<OrderDto>(createdOrderDto, HttpStatus.CREATED);
	}
	
//	remove order
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{orderId}")
	public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId)
	{
		this.orderService.removeOrder(orderId);
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
		.message("Order is Removed..!!")
		.success(true)
		.status(HttpStatus.OK)
		.build();
		
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage,HttpStatus.OK);
	}
	
//	Get orders of the user
	@GetMapping("/users/{userId}")
	public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId){
		List<OrderDto> ordersOfUser = this.orderService.getOrdersOfUser(userId);
		return new ResponseEntity<List<OrderDto>>(ordersOfUser,HttpStatus.OK);
	}
	
//	Get orders of all Users
	@GetMapping
	public ResponseEntity<PageableResponse<OrderDto>> getOrdersOfAllUser(
			@RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
			@RequestParam(value="pageSize",defaultValue = "50",required = false) int pageSize,
			@RequestParam(value="sortBy",defaultValue = "billingName",required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

		PageableResponse<OrderDto> pageableResponse = this.orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
		
		return new ResponseEntity<PageableResponse<OrderDto>>(pageableResponse,HttpStatus.OK); 
			
			
	
	
	
	}
}

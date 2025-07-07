package com.lcwd.electronic.store.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateOrderRequest {

	@NotBlank(message = "Cart Id is Required..!!")
	private String cartId;
	@NotBlank(message = "User Id is Required..!!")
	private String userId;
	private String orderStatus = "PENDING";
	private String paymentStatus = "NOTPAID";
	@NotBlank(message = "Billing Address is Required..!!")
	private String billingAddress;
	@NotBlank(message = "Billing Phone is Required..!!")
	private String billingPhone;
	@NotBlank(message = "Billing Name is Required..!!")
	private String billingName;

}

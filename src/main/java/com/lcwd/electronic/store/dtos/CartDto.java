package com.lcwd.electronic.store.dtos;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {

	private String cartId;
	
	private Date createdAt;
	
	private UserDto user;
	
	private List<CartItemDto> items= new ArrayList<>();
	
	
	
}

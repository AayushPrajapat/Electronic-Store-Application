package com.lcwd.electronic.store.dtos;

import java.util.HashSet;
import java.util.Set;

import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.validate.ImageNameValid;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class UserDto {

	private String userId;
	
	@Size(min = 3,max = 20,message = "Invalid Name !!")
	private String name;
	
	//@Email(message = "Invalid User Email !!")
	@Pattern(
		    regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", 
		    message = "Invalid User Email !!"
		)
		@NotBlank(message = "Email is Blank !!")
		private String email;
	
	@NotBlank
	private String password;

	@Size(min = 4,max = 6,message = "Invalid Gender !!")
	private String gender;

	@NotBlank(message = "Write Something about Yourself !!")
	private String about;

	@ImageNameValid
	private String imageName;
	
	private Set<RoleDto> roles = new HashSet<>();

}

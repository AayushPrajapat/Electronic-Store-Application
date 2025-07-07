package com.lcwd.electronic.store.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
	
	private String categoryId;
//  @min Annotation simply means that if i give the min=4 character it means that only allowed character 4 it cannot be extended....
	@NotBlank
	@Size(min = 4,message = "Title Must be of minimum 4 characters !!")
	private String title;

	@NotBlank(message = "Description Requried !!")
	private String description;

	private String coverImage;

}

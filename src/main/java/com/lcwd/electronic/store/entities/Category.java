package com.lcwd.electronic.store.entities;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "categories")
public class Category {

	@Id
	@Column(name = "id")
	private String categoryId;

	@Column(name="category_title",length = 60, nullable = false)
	private String title;

	@Column(name = "category_desc",length = 500)
	private String description;

	private String coverImage;
	
	/*fetch = FetchType.LAZY means jab hum category fetch krege toh uss time products naa fetch ho!!*/
	/*cascade = CascadeType.ALL means agr hum category delete krege toh products bhi delete honge or save,update ho orr many other operations*/
	@OneToMany(mappedBy = "category",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<Product> products = new ArrayList<>();

}

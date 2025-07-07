package com.lcwd.electronic.store.helper;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableResponse<T> {

	private List<T> content;
	private int pageNo;
	private int pageSize; //(pageSize means that no.of users FOR EX:- if value of pageSize is 2 then 2 users will show...)
	private long totalElements;
	private long totalPages;
	private boolean lastPage;// (if lastPage value is true it means that you are on last page..)

}

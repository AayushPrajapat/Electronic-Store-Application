package com.lcwd.electronic.store.helper;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

public class Helper {

	public static <U, V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type) {
		/*
		 * U means Entity 
		 * V means Dto
		 */

		List<U> entity = page.getContent();

		List<V> dtoList = entity.stream().map(object -> new ModelMapper().map(object, type))
				.collect(Collectors.toList());

		PageableResponse<V> response = new PageableResponse<V>();
		response.setContent(dtoList);
		response.setPageNo(page.getNumber());
		response.setPageSize(page.getSize());
		response.setTotalElements(page.getTotalElements());
		response.setTotalPages(page.getTotalPages());
		response.setLastPage(page.isLast());

		return response;

	}

}

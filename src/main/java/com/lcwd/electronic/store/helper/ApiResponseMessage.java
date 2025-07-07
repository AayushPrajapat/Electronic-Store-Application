package com.lcwd.electronic.store.helper;

import org.springframework.http.HttpStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ApiResponseMessage {

	private String message;
	private boolean success;
	private HttpStatus status;

}

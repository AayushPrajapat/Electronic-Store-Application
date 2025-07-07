package com.lcwd.electronic.store.services;

import java.util.List;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.helper.PageableResponse;

public interface UserService {
	
//	create
	UserDto createUser(UserDto userDto);
//	update
	UserDto updateUser(UserDto userDto,String userId);
//	detete
	void delete(String userId);
//	getAllUsers
	PageableResponse<UserDto> getAllUsers(int pageNumber,int pageSize,String sortBy,String sortDir);
//	getSingleUserById
	UserDto getSingleUserById(String userId);
//	getSingleUserByEmail
	UserDto getSingleUserByEmail(String email);
//	getSearchUser
	List<UserDto> searchUser(String keyWord);
	
//	other user specific features
	
		

}

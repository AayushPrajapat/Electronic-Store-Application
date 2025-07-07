package com.lcwd.electronic.store.controllers;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.helper.ApiResponseMessage;
import com.lcwd.electronic.store.helper.ImageResponse;
import com.lcwd.electronic.store.helper.PageableResponse;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${user.profile.image.path}")
	private String imagesUploadPath;  //application.properties file se dynamically value aa jayegi iss vale variable ke ander

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

//  createUser
//	http://localhost:8080/users
	@PostMapping
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		UserDto createdUser = this.userService.createUser(userDto);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}
//	updateUser	
//	http://localhost:8080/users/{ID}
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(
		@Valid	@RequestBody UserDto userDto,
			@PathVariable("userId") String userId){
		UserDto updatedUser = this.userService.updateUser(userDto, userId);
		return new ResponseEntity<UserDto>(updatedUser,HttpStatus.OK);
	}
//	deleteUser
//	http://localhost:8080/users/{ID}
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String userId)
	{
		this.userService.delete(userId);
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
		.message("User is SuccessFully Deleted!!")
		.success(true)
		.status(HttpStatus.OK).build();
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage,HttpStatus.OK);
	}
//	getAllUsers
//	http://localhost:8080/users
	@GetMapping
//	@ApiOperation(value = "Get All Users",response = ResponseEntity.class,tags = {"User Controller","User APIs"})
	public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
			@RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
			@RequestParam(value="pageSize",defaultValue = "50",required = false) int pageSize,
			@RequestParam(value="sortBy",defaultValue = "name",required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
//		List<UserDto> userDtos = this.userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir);
		 PageableResponse<UserDto> allUsers = this.userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir);
		return new ResponseEntity<PageableResponse<UserDto>>(allUsers,HttpStatus.OK);
	}
//	getSingleUserById
//	http://localhost:8080/users/{ID}
	
	@GetMapping("/{userId}")
	//@ApiOperation(value = "Get Single User by ID")
	public ResponseEntity<UserDto> getUserById(@PathVariable String userId){
		UserDto userDto = this.userService.getSingleUserById(userId);
		return new ResponseEntity<UserDto>(userDto,HttpStatus.OK);
	}
//	getSingleUserByEmail
//	http://localhost:8080/users/email/{email ID}
	@GetMapping("/email/{email}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
		return new ResponseEntity<UserDto>(userService.getSingleUserByEmail(email),HttpStatus.OK);
	}
//	getUserBysearch
//	http://localhost:8080/users/search/{keyword}
	@GetMapping("/search/{keyword}")
	public ResponseEntity<List<UserDto>> getUserBySearch(@PathVariable String keyword){
		return new ResponseEntity<List<UserDto>>(userService.searchUser(keyword),HttpStatus.OK);
	}
	
//	Upload user image
//	http://localhost:8080/users/image/{userId}
	@PostMapping("/image/{userId}")
	public ResponseEntity<ImageResponse> uploadUserImage(
			@RequestParam("userImage") MultipartFile image,
			@PathVariable("userId") String userId, HttpSession session
			) throws IOException{
		
		
		String imageName = this.fileService.uplaodImage(image, imagesUploadPath);
		
		UserDto user = this.userService.getSingleUserById(userId);
		user.setImageName(imageName);
		
		UserDto updateUserDto = this.userService.updateUser(user, userId);
		
		//String realPath = session.getServletContext().getRealPath("/");
		
		ImageResponse imageResponse = ImageResponse.builder()
		.imageName(imageName)
		.success(true)
		.status(HttpStatus.OK)
		.message("User Image SuccessFully write !!"+image.getSize())
		//.message(realPath)
		.build();
		
		return new ResponseEntity<ImageResponse>(imageResponse,HttpStatus.OK);
		
	}
		
//	serve user image
//	http://localhost:8080/users/image/{userId}
	@GetMapping("/image/{userId}")
	public void serveUserImage(@PathVariable String userId,HttpServletResponse response) throws IOException {
		
		UserDto userDto = userService.getSingleUserById(userId);
		log.info("User Image Name : {}",userDto.getImageName());
		
		InputStream resource = fileService.getResource(imagesUploadPath,userDto.getImageName());
		
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
	
		org.springframework.util.StreamUtils.copy(resource,
				response.getOutputStream());
	}

	
	
}

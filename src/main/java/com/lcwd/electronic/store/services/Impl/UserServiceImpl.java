package com.lcwd.electronic.store.services.Impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.helper.PageableResponse;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper mapper;

	@Value("${user.profile.image.path}")
	private String imagePath;
	
	@Value("${normal.role.id}")
	private String roleNormalId;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public UserDto createUser(UserDto userDto) {
//		generate UserId
		String userId = UUID.randomUUID().toString();
		userDto.setUserId(userId);
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
//		dto->entity
		User user = userDtoToEntity(userDto);
//		fetch the role of normal and set it to user...
		Role role = this.roleRepository.findById(roleNormalId).get();
//		set the role of user
		user.getRoles().add(role);
//		saveUser
		User savedUser = this.userRepository.save(user);
//		entity->dto
		UserDto newDto = entityToUserDto(savedUser);
		return newDto;
	}

	@Override
	public UserDto updateUser(UserDto userDto, String userId) {

		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with this userId " + userId));

		user.setName(userDto.getName());
		user.setAbout(userDto.getAbout());
		user.setGender(userDto.getGender());
		user.setImageName(userDto.getImageName());
		user.setPassword(userDto.getPassword());
//		email update
		User updatedUser = this.userRepository.save(user);
		UserDto updatedDto = entityToUserDto(updatedUser);

		return updatedDto;
	}

	@Override
	public void delete(String userId) {

		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given userId " + userId));

		String imageName = user.getImageName();
//		delete  user profile image 
//		EX:- images/user/abc.png
		String fullPath = imagePath + imageName;

		try {

			Path path = Paths.get(fullPath);

			Files.delete(path);

		} catch (NoSuchFileException ex) {
			log.info("User image not found in folder..!!");
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.userRepository.delete(user);
	}

	@Override
	public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {

		// pageNumber default starts from 0

		// Sort sort = Sort.by(sortBy);
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<User> page = this.userRepository.findAll(pageable);

		/*
		 * List<User> pagesUsers = page.getContent(); List<UserDto> userDtos =
		 * pagesUsers.stream().map(user->
		 * entityToUserDto(user)).collect(Collectors.toList());
		 * PageableResponse<UserDto> response = new PageableResponse<UserDto>();
		 * response.setContent(userDtos); response.setPageNo(page.getNumber());
		 * response.setPageSize(page.getSize());
		 * response.setTotalElements(page.getTotalElements());
		 * response.setTotalPages(page.getTotalPages());
		 * response.setLastPage(page.isLast());
		 */

		PageableResponse<UserDto> pageableResponse = Helper.getPageableResponse(page, UserDto.class);

		return pageableResponse;
	}

	@Override
	public UserDto getSingleUserById(String userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given userId " + userId));
		return entityToUserDto(user);
	}

	@Override
	public UserDto getSingleUserByEmail(String email) {
		User user = this.userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given Email " + email));
		return entityToUserDto(user);
	}

	@Override
	public List<UserDto> searchUser(String keyWord) {
		List<User> users = this.userRepository.findByNameContaining(keyWord);
		List<UserDto> userDtos = users.stream().map(user -> entityToUserDto(user)).collect(Collectors.toList());
		return userDtos;
	}

	private User userDtoToEntity(UserDto userDto) {
		/*
		 * User user = User.builder() .userId(userDto.getUserId())
		 * .name(userDto.getName()) .email(userDto.getEmail())
		 * .password(userDto.getPassword()) .gender(userDto.getGender())
		 * .about(userDto.getAbout()) .imageName(userDto.getImageName()).build();
		 */
		User user = mapper.map(userDto, User.class);

		return user;
	}

	private UserDto entityToUserDto(User user) {
		/*
		 * UserDto userDto = UserDto.builder() .userId(user.getUserId())
		 * .name(user.getName()) .email(user.getEmail()) .password(user.getPassword())
		 * .gender(user.getGender()) .about(user.getAbout())
		 * .imageName(user.getImageName()).build();
		 */
		UserDto userDto = mapper.map(user, UserDto.class);
		return userDto;
	}

}

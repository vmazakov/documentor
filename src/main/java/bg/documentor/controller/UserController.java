package bg.documentor.controller;

import bg.documentor.dto.UserDto;
import bg.documentor.dto.UserRegisterDto;
import bg.documentor.mapper.UserMapper;
import bg.documentor.model.User;
import bg.documentor.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j @RestController @RequestMapping(path = "/users") public class UserController {

	private final UserService userService;
	private final UserMapper userMapper;

	public UserController(UserService userService, UserMapper userMapper) {
		this.userService = userService;
		this.userMapper = userMapper;
	}

	@ApiOperation(value = "Register user") @PostMapping("/register") public ResponseEntity<?> saveUser(
			@RequestBody UserRegisterDto userDto) {
		log.debug("POST for registering new user.");

		User user = userMapper.toUserRegister(userDto);

		return ResponseEntity.ok(userService.save(user));
	}

	@PutMapping public ResponseEntity<Object> update(@RequestBody UserDto userDto) {
		log.debug("PUT for update a user.");

		User user = userMapper.toUser(userDto);
		userService.update(user);

		return ResponseEntity.ok().build();
	}

	@GetMapping public ResponseEntity<List<User>> getAllUsers() {
		log.debug("GET getAllUsers.");

		return ResponseEntity.ok(userService.findAll());
	}

	@GetMapping("/{id}") public ResponseEntity<User> getUserById(@PathVariable Long id) {
		log.debug("GET getUserById.");
		return ResponseEntity.ok(userService.findById(id));
	}

}

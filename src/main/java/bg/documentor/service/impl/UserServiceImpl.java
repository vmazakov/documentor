package bg.documentor.service.impl;

import bg.documentor.mapper.UserMapper;
import bg.documentor.model.Role;
import bg.documentor.model.User;
import bg.documentor.repository.UserRepository;
import bg.documentor.service.RoleService;
import bg.documentor.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Set;

/**
 * Service with repository access to Users object
 */
@Slf4j @Service public class UserServiceImpl implements UserService {

	private static final String USER_ROLE = "USER";

	private final UserMapper userMapper;
	private final UserRepository userRepository;
	private final RoleService roleService;
	private final PasswordEncoder encoder;

	@Autowired public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, RoleService roleService,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.roleService = roleService;
		this.encoder = passwordEncoder;
	}

	@Override public User save(User user) {
		if (userRepository.existsByUsername(user.getUsername())) {
			throw new EntityExistsException("Username already exists.");
		}

		log.debug("Registering user with username:{}", user.getUsername());

		Role userRole = roleService.findByDescription(USER_ROLE);

		user.setRoles(Set.of(userRole));
		user.setPassword(encoder.encode(user.getPassword()));

		return userRepository.save(user);
	}

	@Override public void update(User user) {
		userRepository.findById(user.getId())
				.orElseThrow(() -> new IllegalArgumentException("Missing Form Template with Id = " + user.getId()));

		userRepository.save(user);

	}

	@Override public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override public User findById(Long usersId) {
		return userRepository.findById(usersId)
				.orElseThrow(() -> new IllegalArgumentException("Missing Form Template with Id = " + usersId));
	}

	@Override public User findByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("Missing Form Template with username = " + username));
	}

}

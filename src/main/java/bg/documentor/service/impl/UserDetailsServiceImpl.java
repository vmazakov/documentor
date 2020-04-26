package bg.documentor.service.impl;

import bg.documentor.model.Role;
import bg.documentor.model.User;
import bg.documentor.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.core.userdetails.User.withUsername;

@Slf4j @Service public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override @Transactional(readOnly = true) public UserDetails loadUserByUsername(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found."));

		log.debug("Found user: {}", user.getUsername());

		return withUsername(username).password(user.getPassword())
				.roles(user.getRoles().stream().map(Role::getDescription).toArray(String[]::new)).build();
	}
}

package bg.documentor.config.security;

import bg.documentor.model.User;
import bg.documentor.repository.UserRepository;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
@Component public class SpringSecurityAuditorAware implements AuditorAware<Long> {

	private final UserRepository userRepository;

	public SpringSecurityAuditorAware(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override public Optional<Long> getCurrentAuditor() {
		//Could be improved/optimized by using externalUUID/username and extracting it from security context
		return ofNullable(SecurityContextHolder.getContext().getAuthentication())
				.flatMap(authentication -> userRepository.findByUsername(authentication.getName()).map(User::getId))
				.or(() -> Optional.of(1L)); //TODO: for anonymous user
	}
}

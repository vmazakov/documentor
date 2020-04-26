package bg.documentor.config;

import bg.documentor.config.security.SpringSecurityAuditorAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration @EnableJpaAuditing(auditorAwareRef = "auditorAware") public class JpaConfig {

	private final SpringSecurityAuditorAware auditorAware;

	@Autowired public JpaConfig(SpringSecurityAuditorAware auditorAware) {
		this.auditorAware = auditorAware;
	}

	@Bean public AuditorAware<Long> auditorAware() {
		return auditorAware;
	}
}

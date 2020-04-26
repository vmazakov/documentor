package bg.documentor.config.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data @Validated @Configuration @ConfigurationProperties(prefix = "auth") public class AuthProperties {

	@NotBlank private String authoritiesKey;

	@NotBlank private String jwtSecret;

	@NotBlank private String corsOrigins;
}

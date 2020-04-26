package bg.documentor.config.security.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString(exclude = "password") @NoArgsConstructor @AllArgsConstructor public class JwtRequest {

	private String username;
	private String password;
}

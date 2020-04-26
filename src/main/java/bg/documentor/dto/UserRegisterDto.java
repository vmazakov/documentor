package bg.documentor.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder public class UserRegisterDto {

	private String username;

	private String password;

	private String firstName;

	private String lastName;

}

package bg.documentor.dto;

import bg.documentor.enums.Validator;
import lombok.Builder;
import lombok.Data;

@Data @Builder public class FieldValidatorDto {

	private Long id;

	private Validator key;

	private String value;

}

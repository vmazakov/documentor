package bg.documentor.dto;

import bg.documentor.enums.SubType;
import bg.documentor.enums.Type;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * DTO for Form's Field
 */
@Data @Builder public class FormFieldDto {

	private Long id;

	private String value;

	private String key;

	private String label;

	private Integer order;

	private Type type;

	private SubType subType;

	private List<FieldOptionDto> options;

	private List<FieldValidatorDto> validators;

}

package bg.documentor.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * DTO for Form Template
 */
@Data @Builder public class FormTemplateDto {

	private Long id;

	private String key;

	private String value;

	private List<FormFieldDto> formFields;

}
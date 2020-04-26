package bg.documentor.dto;

import lombok.Builder;
import lombok.Data;

/**
 * History Entity Field in a FormTemplate
 */
@Data @Builder public class FormFieldHistDto {

	private Long id;

	/* initial empty Value of a field */
	private String value;

	/* The object FormField would be extracted from String key, as it is unique for the particular formControl*/
	private Long formFieldId;

	private String formFieldKey;

	private Long formTemplateId;

	/*  file name */
	private String formTemplateValue;

}

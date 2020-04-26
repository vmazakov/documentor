package bg.documentor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor public class FormTemplateListDto {

	private Long id;
	private String key;
	private String value;
}

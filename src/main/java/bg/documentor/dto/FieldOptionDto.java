package bg.documentor.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder public class FieldOptionDto {

	private Long id;

	private String key;

	private String value;

}

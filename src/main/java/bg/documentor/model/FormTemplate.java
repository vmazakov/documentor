package bg.documentor.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data @Entity @Builder @ToString @NoArgsConstructor @AllArgsConstructor @Table(name = "form_template") public class FormTemplate
		extends AbstractAuditingEntity {

	/**
	 * Unique id
	 */
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

	/**
	 * Key of the template
	 */
	@Column(name = "template_key") private String key;

	/**
	 * Name of the template
	 */
	@Column(name = "template_name") private String value;

	/**
	 * relation to child records in Fields entity //fetch = FetchType.EAGER,
	 */
	@OneToMany(mappedBy = "formTemplate", cascade = CascadeType.ALL, orphanRemoval = true) private List<FormField> formFields;

	@PostPersist public void onPostPersist() {
		setKey(id.toString());
	}

}


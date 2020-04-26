package bg.documentor.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * History Entity Field in a FormTemplate
 */

@Data @Entity @ToString(exclude = { "formTemplate",
		"user" }) @Table(name = "form_field_hist") public class FormFieldHist extends AbstractAuditingEntity {

	/**
	 * Unique ID
	 */
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

	/**
	 * initial empty Value of a field
	 */
	@Column(name = "field_value") private String value;

	/**
	 * FK column to parent entity FormField
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) @JoinColumn(name = "form_field_id") private FormField formField;

	/**
	 * FK column to parent entity FormTemplate
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) @JoinColumn(name = "form_template_id") private FormTemplate formTemplate;

}

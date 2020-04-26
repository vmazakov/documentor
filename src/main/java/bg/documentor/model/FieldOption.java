package bg.documentor.model;

import lombok.*;

import javax.persistence.*;

/**
 * Entity Option - in case of Field type "SELECT", options are elements in the rendered drop-down list
 */
@Data @Entity @Builder @ToString(exclude = {
		"formField" }) @AllArgsConstructor @NoArgsConstructor @Table(name = "field_option") public class FieldOption
		extends AbstractAuditingEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

	/**
	 * Unique key
	 */
	@Column(name = "option_key") private String key;

	/**
	 * Value of an option
	 */
	@Column(name = "option_value") private String value;

	@ManyToOne @JoinColumn(name = "form_field_id") private FormField formField;

}

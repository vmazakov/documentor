package bg.documentor.model;

import bg.documentor.enums.Validator;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * Entity Validator of a FormField
 */
@Data @Entity @ToString(exclude = "formField") @Table(name = "field_validator") public class FieldValidator
		extends AbstractAuditingEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

	/**
	 * Unique key
	 */
	@Enumerated(EnumType.STRING) @Column(name = "validator_key") private Validator key;

	/**
	 * Value of a Validator - enumerated
	 */
	@Column(name = "validator_value") private String value;

	@ManyToOne @JoinColumn(name = "form_field_id") private FormField formField;

}

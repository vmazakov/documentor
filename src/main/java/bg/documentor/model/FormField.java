package bg.documentor.model;

import bg.documentor.enums.SubType;
import bg.documentor.enums.Type;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Entity Field in a FormTemplate
 */
@Data @Entity @Builder
//@ToString(exclude = {"formTemplate"})
@ToString @NoArgsConstructor @AllArgsConstructor @Table(name = "form_field") public class FormField
		extends AbstractAuditingEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

	/**
	 * Initial empty Value of a field
	 */
	@Column(name = "field_value") private String value;

	/**
	 * Unique key
	 */
	@Column(name = "field_key") private String key;

	/**
	 * Label of a field
	 */
	@Column(name = "field_label") private String label;

	/**
	 * Field Order
	 */
	@Column(name = "field_order") private Integer order;

	@Enumerated(EnumType.STRING) @Column(name = "field_type") private Type type;

	@Enumerated(EnumType.STRING) @Column(name = "field_subType") private SubType subType;

	/**
	 * FK column to parent entity FormTemplate
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) @JoinColumn(name = "form_template_id") private FormTemplate formTemplate;

	/**
	 * FK column to child entity Options
	 */
	@OneToMany(mappedBy = "formField", cascade = CascadeType.ALL, orphanRemoval = true) private List<FieldOption> options;

	/**
	 * FK column to child entity Validators
	 */
	@OneToMany(mappedBy = "formField", cascade = CascadeType.ALL, orphanRemoval = true) private List<FieldValidator> validators;

}

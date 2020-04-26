package bg.documentor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data @Entity @Builder @NoArgsConstructor @AllArgsConstructor @Table(name = "roles") public class Role
		extends AbstractAuditingEntity {

	@Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;

	@Column(name = "description", nullable = false) private String description;

}

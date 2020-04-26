package bg.documentor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Data @ToString(exclude = {
		"password" }) @NoArgsConstructor @AllArgsConstructor @Entity @Table(name = "users") public class User
		extends AbstractAuditingEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

	@Column private String username;

	@Column private String password;

	@Column(name = "first_name") private String firstName;

	@Column(name = "last_name") private String lastName;

	@ManyToMany @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id")) private Set<Role> roles;

}

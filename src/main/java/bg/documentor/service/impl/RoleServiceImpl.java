package bg.documentor.service.impl;

import bg.documentor.model.Role;
import bg.documentor.repository.RoleRepository;
import bg.documentor.service.RoleService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override public Role findByDescription(String description) {
		return roleRepository.findByDescriptionIgnoreCase(description)
				.orElseThrow(() -> new EntityNotFoundException("Given role: [" + description + " ] is not present."));
	}
}

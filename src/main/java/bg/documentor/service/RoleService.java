package bg.documentor.service;

import bg.documentor.model.Role;

public interface RoleService {

    Role findByDescription(String description);
}

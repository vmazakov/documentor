package bg.documentor.service;

import bg.documentor.model.User;

import java.util.List;

/**
 * Service to access the FormTemplate Object in the data layer.
 */
public interface UserService {

	/**
	 * Persist User Object
	 *
	 * @param user Object
	 * @return User
	 */
	User save(User user);

	/**
	 * Persist changes to Users Object
	 *
	 * @param user
	 */
	void update(User user);

	/**
	 * Reads all Userss from DB
	 *
	 * @return parent Userss and its child records
	 */
	List<User> findAll();

	User findById(Long UsersId);

	User findByUsername(String username);

}

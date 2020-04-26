package bg.documentor.service;

import bg.documentor.dto.FieldOptionDto;
import bg.documentor.model.FieldOption;

/**
 * Service to access the Option Object in the data layer.
 */
public interface FieldOptionService {

	/**
	 * Persist Option Object
	 *
	 * @param dto OptionsDto
	 * @return
	 */
	FieldOption save(FieldOptionDto dto);

	/**
	 * Persist changes to Option Object
	 *
	 * @param dto OptionsDto
	 */
	void update(FieldOptionDto dto);

}

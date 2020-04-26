package bg.documentor.service;

import bg.documentor.dto.FieldValidatorDto;
import bg.documentor.model.FieldValidator;

/**
 * Service to access the Validator Object in the data layer.
 */
public interface FieldValidatorService {

	/**
	 * Persist Validation Object
	 *
	 * @param dto ValidatorsDto
	 * @return Validators
	 */
	FieldValidator save(FieldValidatorDto dto);

	/**
	 * Persist changes to Validation Object
	 *
	 * @param dto ValidatorsDto
	 */
	void update(FieldValidatorDto dto);

}

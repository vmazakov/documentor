package bg.documentor.service;

import bg.documentor.dto.FormFieldDto;
import bg.documentor.model.FormField;

import java.util.List;

/**
 * Service to access the FormField Object in the data layer.
 */
public interface FormFieldService {

	List<FormField> findAll();

	/**
	 * Persist FormField Object
	 *
	 * @param dto FormFieldDto
	 * @return FormField
	 */
	FormField save(FormFieldDto dto);

	/**
	 * Persist changes to FormField Object
	 *
	 * @param dt FormFieldDto
	 */
	void update(FormFieldDto dt);

	List<FormField> findByFormTemplateId(Long formTemplateId);

}

package bg.documentor.service;

import bg.documentor.model.FormTemplate;

import java.util.List;

/**
 * Service to access the FormTemplate Object in the data layer.
 */
public interface FormTemplateService {

	/**
	 * Persist FormTemplate Object
	 *
	 * @param formTemplate Object
	 * @return FormTemplate
	 */
	FormTemplate save(FormTemplate formTemplate);

	/**
	 * Persist changes to FormTemplate Object
	 *
	 * @param formTemplate FormTemplate
	 */
	void update(FormTemplate formTemplate);

	/**
	 * Reads all FormTemplates from DB
	 *
	 * @return parent FormTemplates and its child records
	 */

	List<FormTemplate> findAll();

	FormTemplate findById(Long formTemplateId);

}

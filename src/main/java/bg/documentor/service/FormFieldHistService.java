package bg.documentor.service;

import bg.documentor.dto.FormFieldHistDto;
import bg.documentor.model.FormFieldHist;

import java.util.List;

/**
 * Service to access the FormField Object in the data layer.
 */
public interface FormFieldHistService {

	List<FormFieldHist> findAll();

	/**
	 * Persist FormFieldHist Object
	 *
	 * @param
	 * @return FormFieldHist
	 */
	FormFieldHist save(FormFieldHist formFieldHist);

	/**
	 * Persist FormFieldHist Object
	 *
	 * @param formFieldHistList FormFieldHistDto
	 */
	List<FormFieldHist> saveAll(List<FormFieldHist> formFieldHistList);

	/**
	 * Persist changes to FormFieldHist Object
	 *
	 * @param dt FormFieldHistDto
	 */
	void update(FormFieldHistDto dt);

	List<FormFieldHist> findByFormTemplateId(Long formTemplateId);

}

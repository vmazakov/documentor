package bg.documentor.service.impl;

import bg.documentor.mapper.FormTemplateMapper;
import bg.documentor.model.FieldOption;
import bg.documentor.model.FieldValidator;
import bg.documentor.model.FormField;
import bg.documentor.model.FormTemplate;
import bg.documentor.repository.FormTemplateRepository;
import bg.documentor.service.FormTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service with repository access to FormTemplate object
 */
@Service public class FormTemplateServiceImpl implements FormTemplateService {

	private final FormTemplateMapper formTemplateMapper;
	private final FormTemplateRepository formTemplateRepository;

	@Autowired public FormTemplateServiceImpl(FormTemplateRepository formTemplateRepository,
			FormTemplateMapper formTemplateMapper) {
		this.formTemplateRepository = formTemplateRepository;
		this.formTemplateMapper = formTemplateMapper;
	}

	@Override public FormTemplate save(FormTemplate formTemplate) {
		/*set parent to each field*/
		List<FormField> formFieldList = formTemplate.getFormFields();
		formFieldList.forEach(field -> {
			/*set parent to each field*/
			field.setFormTemplate(formTemplate);
			/*set parent to each option*/
			List<FieldOption> fieldOptionList = field.getOptions();
			fieldOptionList.forEach(option -> option.setFormField(field));
			/*set parent to each validator*/
			List<FieldValidator> fieldValidatorList = field.getValidators();
			fieldValidatorList.forEach(option -> option.setFormField(field));
		});

		return formTemplateRepository.save(formTemplate);
	}

	@Override public void update(FormTemplate formTemplate) {
		formTemplateRepository.findById(formTemplate.getId()).orElseThrow(
				() -> new IllegalArgumentException("Missing Form Template with Id = " + formTemplate.getId()));

		formTemplateRepository.save(formTemplate);

	}

	@Override public List<FormTemplate> findAll() {
		return formTemplateRepository.findAll();
	}

	@Override @Transactional(readOnly = true) public FormTemplate findById(Long formTemplateId) {
		FormTemplate formTemplate = formTemplateRepository.findById(formTemplateId)
				.orElseThrow(() -> new IllegalArgumentException("Missing Form Template with Id = " + formTemplateId));

		formTemplate.getFormFields().forEach(formField -> {
			formField.getOptions().size();
			formField.getValidators().size();
		});
		return formTemplate;
	}
}

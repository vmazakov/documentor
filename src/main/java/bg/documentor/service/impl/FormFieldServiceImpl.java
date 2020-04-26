package bg.documentor.service.impl;

import bg.documentor.dto.FormFieldDto;
import bg.documentor.mapper.FormFieldMapper;
import bg.documentor.model.FieldOption;
import bg.documentor.model.FieldValidator;
import bg.documentor.model.FormField;
import bg.documentor.repository.FormFieldRepository;
import bg.documentor.repository.FormTemplateRepository;
import bg.documentor.service.FormFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service with repository access to FormField object
 */
@Service public class FormFieldServiceImpl implements FormFieldService {

	public final FormTemplateRepository formTemplateRepository;
	private final FormFieldMapper formFieldMapper;
	private final FormFieldRepository formFieldRepository;

	@Autowired public FormFieldServiceImpl(FormFieldRepository formFieldRepository, FormFieldMapper formFieldMapper,
			FormTemplateRepository formTemplateRepository) {
		this.formFieldRepository = formFieldRepository;
		this.formFieldMapper = formFieldMapper;
		this.formTemplateRepository = formTemplateRepository;
	}

	@Override public List<FormField> findAll() {
		return formFieldRepository.findAll();
	}

	@Override public FormField save(FormFieldDto dto) {

		FormField formField = formFieldMapper.toFormField(dto);

		/*set parent to each option*/
		List<FieldOption> fieldOptionList = formField.getOptions();
		fieldOptionList.forEach(option -> option.setFormField(formField));
		/*set parent to each validator*/
		List<FieldValidator> fieldValidatorList = formField.getValidators();
		fieldValidatorList.forEach(option -> option.setFormField(formField));

		return formFieldRepository.save(formField);
	}

	@Override public void update(FormFieldDto dto) {
		Optional<FormField> formFieldOptional = formFieldRepository.findById(dto.getId());
		if (formFieldOptional.isEmpty()) {
			throw new IllegalArgumentException("Missing Form Field with Id = " + dto.getId());
		}

		FormField formFields = formFieldMapper.toFormField(dto);

		formFieldRepository.save(formFields);
	}

	@Override public List<FormField> findByFormTemplateId(Long formTemplateId) {
		return formFieldRepository.findByFormTemplateId(formTemplateId);
	}

}

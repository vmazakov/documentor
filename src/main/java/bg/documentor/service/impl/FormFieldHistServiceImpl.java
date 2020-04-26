package bg.documentor.service.impl;

import bg.documentor.dto.FormFieldHistDto;
import bg.documentor.mapper.FormFieldHistMapper;
import bg.documentor.model.FormFieldHist;
import bg.documentor.repository.FormFieldHistRepository;
import bg.documentor.repository.FormTemplateRepository;
import bg.documentor.service.FormFieldHistService;
import bg.documentor.service.UserService;
import bg.documentor.util.WordDocumentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service with repository access to FormField object
 */
@Slf4j @Service public class FormFieldHistServiceImpl implements FormFieldHistService {

	public final WordDocumentProcessor wordDocumentProcessor;
	public final FormTemplateRepository formTemplateRepository;
	public final FormFieldHistRepository formFieldHistRepository;
	private final FormFieldHistMapper formFieldHistMapper;
	private final UserService userService;

	public FormFieldHistServiceImpl(FormFieldHistRepository formFieldHistRepository,
			FormFieldHistMapper formFieldHistMapper, UserService userService,
			FormTemplateRepository formTemplateRepository, WordDocumentProcessor wordDocumentProcessor) {
		this.formFieldHistRepository = formFieldHistRepository;
		this.formFieldHistMapper = formFieldHistMapper;
		this.userService = userService;
		this.formTemplateRepository = formTemplateRepository;
		this.wordDocumentProcessor = wordDocumentProcessor;
	}

	@Override public List<FormFieldHist> findAll() {
		return formFieldHistRepository.findAll();
	}

	@Override public FormFieldHist save(FormFieldHist formFieldHist) {
		return formFieldHistRepository.save(formFieldHist);
	}

	@Override public List<FormFieldHist> saveAll(List<FormFieldHist> formFieldHistList) {
		log.info("FormFieldHist SaveAll");
		wordDocumentProcessor.generateWordFile(formFieldHistList);

		return formFieldHistRepository.saveAll(formFieldHistList);
	}

	@Override public void update(FormFieldHistDto dto) {

		Optional<FormFieldHist> formFieldHistOptional = formFieldHistRepository.findById(dto.getId());
		if (formFieldHistOptional.isEmpty()) {
			throw new IllegalArgumentException("Missing Form FieldHist with Id = " + dto.getId());
		}
		FormFieldHist formFieldHists = formFieldHistMapper.toFormFieldHist(dto);

		formFieldHistRepository.save(formFieldHists);
	}

	@Override public List<FormFieldHist> findByFormTemplateId(Long formTemplateId) {

		return formFieldHistRepository.findByFormTemplateId(formTemplateId);
	}

}

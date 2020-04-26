package bg.documentor.controller;

import bg.documentor.dto.FormFieldDto;
import bg.documentor.mapper.FormFieldMapper;
import bg.documentor.model.FormField;
import bg.documentor.service.FormFieldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j @RestController @RequestMapping(path = "/form-field") public class FormFieldController {

	private final FormFieldService formFieldService;
	private final FormFieldMapper formFieldMapper;

	public FormFieldController(FormFieldService formFieldService, FormFieldMapper formFieldMapper) {
		this.formFieldService = formFieldService;
		this.formFieldMapper = formFieldMapper;
	}

	@GetMapping public ResponseEntity<Iterable<FormField>> getAllFormFields() {
		return ResponseEntity.ok(formFieldService.findAll());
	}

	@GetMapping("/{id}") public ResponseEntity<List<FormFieldDto>> findByFormTemplateId(@PathVariable Long id) {
		log.debug("Get the list of all FormFields belonging to FormTemplate with ID = " + id);
		List<FormFieldDto> formFieldDtos = formFieldService.findByFormTemplateId(id).stream()
				.map(formFieldMapper::toFormFieldDto).collect(Collectors.toList());
		return ResponseEntity.ok(formFieldDtos);
	}

}

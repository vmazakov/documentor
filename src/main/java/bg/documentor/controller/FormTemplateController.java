package bg.documentor.controller;

import bg.documentor.dto.FormTemplateDto;
import bg.documentor.dto.FormTemplateListDto;
import bg.documentor.mapper.FormTemplateMapper;
import bg.documentor.model.FormTemplate;
import bg.documentor.service.FormTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j @RestController @RequestMapping("/demo/form-template") public class FormTemplateController {

	private final FormTemplateService formTemplateService;
	private final FormTemplateMapper formTemplateMapper;

	public FormTemplateController(FormTemplateService formTemplateService, FormTemplateMapper formTemplateMapper) {
		this.formTemplateService = formTemplateService;
		this.formTemplateMapper = formTemplateMapper;
	}

	@PostMapping public ResponseEntity<?> add(@RequestBody FormTemplateDto formTemplateDto) {
		log.debug("New FormTemplate added");
		FormTemplate formTemplate = formTemplateMapper.toFormTemplate(formTemplateDto);
		formTemplateService.save(formTemplate);
		return ResponseEntity.ok().build();
	}

	@PutMapping public ResponseEntity<?> update(@RequestBody FormTemplateDto formTemplateDto) {
		log.debug("Update the FormTemplate with ID = " + formTemplateDto.getId());
		FormTemplate formTemplate = formTemplateMapper.toFormTemplate(formTemplateDto);
		formTemplateService.update(formTemplate);
		return ResponseEntity.ok().build();
	}

	@GetMapping public ResponseEntity<List<FormTemplateListDto>> getAllFormTemplates() {
		log.debug("Get the list of all FormTemplates");
		List<FormTemplateListDto> formTemplateListDtos = formTemplateService.findAll().stream()
				.map(formTemplateMapper::toFormTemplateListDto).collect(Collectors.toList());

		return ResponseEntity.ok(formTemplateListDtos);
	}

	@GetMapping("/{id}") public ResponseEntity<FormTemplateDto> getFormTemplateById(@PathVariable Long id) {
		log.debug("Get the FormTemplate(alongside its child objects) with ID = " + id);
		FormTemplateDto formTemplateDto = formTemplateMapper.toFormTemplateDto(formTemplateService.findById(id));
		return ResponseEntity.ok(formTemplateDto);
	}

}

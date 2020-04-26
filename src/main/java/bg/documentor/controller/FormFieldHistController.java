package bg.documentor.controller;

import bg.documentor.dto.FormFieldHistDto;
import bg.documentor.mapper.FormFieldHistMapper;
import bg.documentor.model.FormFieldHist;
import bg.documentor.service.FormFieldHistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j @RestController @RequestMapping(path = "/form-field-hist") public class FormFieldHistController {

	private final FormFieldHistService formFieldHistService;
	private final FormFieldHistMapper formFieldHistMapper;

	public FormFieldHistController(FormFieldHistService formFieldHistService, FormFieldHistMapper formFieldHistMapper) {
		this.formFieldHistService = formFieldHistService;
		this.formFieldHistMapper = formFieldHistMapper;
	}

	@PostMapping(path = "/save") public ResponseEntity<?> add(@RequestBody FormFieldHistDto formFieldHistDto) {
		log.debug("POST new formFieldHist added.");
		FormFieldHist formFieldHist = formFieldHistMapper.toFormFieldHist(formFieldHistDto);
		formFieldHistService.save(formFieldHist);
		return ResponseEntity.ok().build();
	}

	@PostMapping(path = "/save-all") public ResponseEntity<?> addAll(
			@RequestBody List<FormFieldHistDto> formFieldHistDtoList) {
		log.debug("POST new List of formFieldHists added.");

		List<FormFieldHist> formFieldHistList = formFieldHistMapper.toFormFieldHists(formFieldHistDtoList);

		formFieldHistService.saveAll(formFieldHistList);
		return ResponseEntity.ok().build();
	}

	@GetMapping(path = "/get") public ResponseEntity<List<FormFieldHist>> getAllFormFieldsHist() {
		log.debug("GET getAllFormFieldsHist.");
		return ResponseEntity.ok(formFieldHistService.findAll());
	}

	@GetMapping("/{id}") public ResponseEntity<List<FormFieldHist>> findByFormTemplateId(@PathVariable Long id) {
		log.debug("GET findByFormTemplateId.");
		return ResponseEntity.ok(formFieldHistService.findByFormTemplateId(id));
	}

}

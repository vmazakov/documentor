package bg.documentor.service.impl;

import bg.documentor.dto.FieldValidatorDto;
import bg.documentor.mapper.FieldValidatorMapper;
import bg.documentor.model.FieldValidator;
import bg.documentor.repository.FieldValidatorRepository;
import bg.documentor.service.FieldValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service with repository access to Validator object
 */
@Service public class FieldValidatorServiceImpl implements FieldValidatorService {

	private final FieldValidatorMapper fieldValidatorMapper;
	private final FieldValidatorRepository fieldValidatorRepository;

	@Autowired public FieldValidatorServiceImpl(FieldValidatorMapper fieldValidatorMapper,
			FieldValidatorRepository fieldValidatorRepository) {
		this.fieldValidatorMapper = fieldValidatorMapper;
		this.fieldValidatorRepository = fieldValidatorRepository;
	}

	@Override public FieldValidator save(FieldValidatorDto dto) {

		FieldValidator fieldValidator = fieldValidatorMapper.toFieldValidator(dto);
		return fieldValidatorRepository.save(fieldValidator);
	}

	@Override public void update(FieldValidatorDto dto) {

		Optional<FieldValidator> fieldValidatorOptional = fieldValidatorRepository.findById(dto.getId());
		if (fieldValidatorOptional.isEmpty()) {
			throw new IllegalArgumentException("Missing Field Validator with Id = " + dto.getId());
		}

		FieldValidator fieldValidator = fieldValidatorMapper.toFieldValidator(dto);
		fieldValidatorRepository.save(fieldValidator);
	}

}

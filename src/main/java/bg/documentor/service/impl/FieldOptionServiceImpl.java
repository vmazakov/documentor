package bg.documentor.service.impl;

import bg.documentor.dto.FieldOptionDto;
import bg.documentor.mapper.FieldOptionMapper;
import bg.documentor.model.FieldOption;
import bg.documentor.repository.FieldOptionRepository;
import bg.documentor.service.FieldOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j @Service public class FieldOptionServiceImpl implements FieldOptionService {

	private final FieldOptionMapper fieldOptionMapper;
	private final FieldOptionRepository fieldOptionRepository;

	public FieldOptionServiceImpl(FieldOptionMapper fieldOptionMapper, FieldOptionRepository fieldOptionRepository) {
		this.fieldOptionMapper = fieldOptionMapper;
		this.fieldOptionRepository = fieldOptionRepository;
	}

	@Override public FieldOption save(FieldOptionDto dto) {

		FieldOption fieldOption = fieldOptionMapper.toFieldOption(dto);
		return fieldOptionRepository.save(fieldOption);
	}

	@Override public void update(FieldOptionDto dto) {
		fieldOptionRepository.findById(dto.getId())
				.orElseThrow(() -> new IllegalArgumentException("Missing Option with Id = " + dto.getId()));

		FieldOption fieldOption = fieldOptionMapper.toFieldOption(dto);
		fieldOptionRepository.save(fieldOption);

	}
}

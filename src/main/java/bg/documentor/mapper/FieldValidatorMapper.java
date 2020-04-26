package bg.documentor.mapper;

import bg.documentor.dto.FieldValidatorDto;
import bg.documentor.model.FieldValidator;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") public interface FieldValidatorMapper {

	FieldValidator toFieldValidator(FieldValidatorDto fieldValidatorDto);
}

package bg.documentor.mapper;

import bg.documentor.dto.FieldOptionDto;
import bg.documentor.model.FieldOption;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") public interface FieldOptionMapper {

	FieldOption toFieldOption(FieldOptionDto fieldOptionDto);
}

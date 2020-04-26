package bg.documentor.mapper;

import bg.documentor.dto.FormFieldDto;
import bg.documentor.model.FormField;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") public interface FormFieldMapper {

	FormField toFormField(FormFieldDto formFieldDto);

	@InheritInverseConfiguration(name = "toFormField") FormFieldDto toFormFieldDto(FormField formField);

}

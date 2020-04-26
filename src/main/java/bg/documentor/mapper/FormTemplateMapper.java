package bg.documentor.mapper;

import bg.documentor.dto.FormTemplateDto;
import bg.documentor.dto.FormTemplateListDto;
import bg.documentor.model.FormTemplate;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") public interface FormTemplateMapper {

	FormTemplateDto toFormTemplateDto(FormTemplate formTemplate);

	@InheritInverseConfiguration(name = "toFormTemplateDto") FormTemplate toFormTemplate(
			FormTemplateDto formTemplateDto);

	FormTemplateListDto toFormTemplateListDto(FormTemplate formTemplate);
}

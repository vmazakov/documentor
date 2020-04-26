package bg.documentor.mapper;

import bg.documentor.dto.FormFieldHistDto;
import bg.documentor.dto.FormTemplateDto;
import bg.documentor.model.FormFieldHist;
import bg.documentor.model.FormTemplate;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring") public interface FormFieldHistMapper {

	@Mapping(target = "formField.key", source = "formFieldKey") @Mapping(target = "formField.id", source = "formFieldId") @Mapping(target = "formTemplate.value", source = "formTemplateValue") @Mapping(target = "formTemplate.id", source = "formTemplateId") FormFieldHist toFormFieldHist(
			FormFieldHistDto formFieldHistDto);

	FormTemplateDto templateMapper(FormTemplate formTemplate);

	@InheritInverseConfiguration(name = "templateMapper") FormTemplate templateMapper(FormTemplateDto formTemplateDto);

	List<FormFieldHist> toFormFieldHists(List<FormFieldHistDto> formFieldHistDtoList);

}

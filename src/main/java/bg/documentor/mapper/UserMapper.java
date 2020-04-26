package bg.documentor.mapper;

import bg.documentor.dto.UserDto;
import bg.documentor.dto.UserRegisterDto;
import bg.documentor.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") public interface UserMapper {

	UserDto toUserDto(User user);

	@InheritInverseConfiguration(name = "toUserDto") User toUser(UserDto userDto);

	UserRegisterDto toUserRegisterDto(User user);

	@InheritInverseConfiguration(name = "toUserRegisterDto") User toUserRegister(UserRegisterDto userRegisterDto);
}

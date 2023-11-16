package pl.empik.rest.userrestservice.mappers;

import org.mapstruct.Mapper;
import pl.empik.rest.userrestservice.dto.LoginRegistryDto;
import pl.empik.rest.userrestservice.model.LoginRegistry;

import java.util.List;

@Mapper
public interface LoginRegistryMapper {
    LoginRegistry loginRegistryDtoToLoginRegistry(LoginRegistryDto loginRegistryDto);
    LoginRegistryDto loginRegistryToLoginRegistryDto(LoginRegistry loginRegistry);

    List<LoginRegistryDto> convertLoginRegistriesToLoginRegistryDtoCollection(List<LoginRegistry> loginRegistries);
}

package pl.empik.rest.userrestservice.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import pl.empik.rest.userrestservice.dto.LoginRegistryDto;
import pl.empik.rest.userrestservice.dto.RepositoryDto;
import pl.empik.rest.userrestservice.dto.UserDto;
import pl.empik.rest.userrestservice.dto.UserResponseDto;
import pl.empik.rest.userrestservice.exception.MyErrorHandler;
import pl.empik.rest.userrestservice.mappers.LoginRegistryMapper;
import pl.empik.rest.userrestservice.model.LoginRegistry;
import pl.empik.rest.userrestservice.repository.LoginRegistryRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final String ALL_USERS_URL = "https://api.github.com/users";
    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder().errorHandler(new MyErrorHandler());

    private UserDto[] allUsers;

    @Autowired
    LoginRegistryRepository loginRegistryRepository;
    @Autowired
    LoginRegistryMapper loginRegistryMapper;

    @PostConstruct
    public void init() {
        allUsers = restTemplateBuilder.build().getForEntity(ALL_USERS_URL, UserDto[].class).getBody();
    }

    public Optional<UserResponseDto> getUserByLogin(String login) {
        Optional<UserDto> userDtoOptional = getAllUsers().stream().filter(c -> c.login.equalsIgnoreCase(login)).findAny();
        if (userDtoOptional.isEmpty()) {
            return Optional.of(new UserResponseDto());
        }
        UserDto existingUser = userDtoOptional.get();
        int followersCount = getFollowersCount(existingUser);
        int repositoriesCount = getRepositoriesCount(existingUser);
        checkAndUpdateLoginRegistry(login);
        return Optional.of(mapToUserResponseDto(existingUser, followersCount, repositoriesCount));
    }

    private void checkAndUpdateLoginRegistry(String login) {
        List<LoginRegistryDto> registriesDto = convertToLoginRegistryDtoList();
        Optional<LoginRegistryDto> loginRegistryDto = getParticularRegistryDtoByLogin(login, registriesDto);

        if (loginRegistryDto.isEmpty()) {
            loginRegistryRepository.save(new LoginRegistry(login, 1L));
        } else {
            loginRegistryDto.get().setRequestCount(loginRegistryDto.get().getRequestCount() + 1);
            loginRegistryRepository.save(loginRegistryMapper.loginRegistryDtoToLoginRegistry(loginRegistryDto.get()));
        }
    }

    private Optional<LoginRegistryDto> getParticularRegistryDtoByLogin(String login, List<LoginRegistryDto> listDto) {
        return listDto.stream().filter(l -> l.getLogin().equalsIgnoreCase(login)).findFirst();
    }

    List<LoginRegistryDto> convertToLoginRegistryDtoList() {
        return loginRegistryMapper.convertLoginRegistriesToLoginRegistryDtoCollection(loginRegistryRepository.findAll());
    }

    private UserResponseDto mapToUserResponseDto(UserDto userToMap, int followersCount, int repositoriesCount) {
        return UserResponseDto.builder()
                .id(Long.valueOf(userToMap.getId()))
                .name(userToMap.login)
                .type(userToMap.type)
                .calculations(getCalculationResult(followersCount, repositoriesCount))
                .creationDate(LocalDate.now())
                .avatarUrl(userToMap.avatar_url)
                .login(userToMap.login).build();
    }

    private double getCalculationResult(int followersCount, int repositoriesCount) {
        double calculation;
        double scale = Math.pow(10, 5);
        if (followersCount != 0) {
            calculation = Math.round(((6.0 / followersCount) * (2 + repositoriesCount)) * scale) / scale;
        } else {
            calculation = 0.0;
        }
        return calculation;
    }

    private int getFollowersCount(UserDto existingUser) {
        if (existingUser.followers_url.isEmpty() || existingUser.followers_url.isBlank()) {
            return 0;
        }
        Object[] followersCollection = restTemplateBuilder
                .build().getForEntity(existingUser.followers_url, Object[].class).getBody();

        if (followersCollection == null) {
            return 0;
        }
        return followersCollection.length;
    }

    private int getRepositoriesCount(UserDto existingUser) {
        if (existingUser.repos_url.isEmpty() || existingUser.repos_url.isBlank()) {
            return 0;
        }
        RepositoryDto[] repositoriesCollection = restTemplateBuilder
                .build().getForEntity(existingUser.repos_url, RepositoryDto[].class).getBody();
        if (repositoriesCollection == null) {
            return 0;
        }
        return Arrays.stream(repositoriesCollection).filter(r -> !r.isPrivate).toList().size();
    }

    private List<UserDto> getAllUsers() {
        if (allUsers == null || allUsers.length == 0) {
            return new ArrayList<>();
        }
        return Arrays.asList(allUsers);
    }
}

package pl.empik.rest.userrestservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LoginRegistryDto {
    private String login;
    private Long requestCount;
}

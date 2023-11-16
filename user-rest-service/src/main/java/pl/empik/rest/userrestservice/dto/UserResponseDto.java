package pl.empik.rest.userrestservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    public Long id;
    public String login;
    public String name;
    public String type;
    public String avatarUrl;
    public LocalDate creationDate;
    public Double calculations;
}

package pl.empik.rest.userrestservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryDto {
    private String id;
    private String node_id;
    private String name;
    private String full_name;
    @JsonProperty("private") public boolean isPrivate;
}

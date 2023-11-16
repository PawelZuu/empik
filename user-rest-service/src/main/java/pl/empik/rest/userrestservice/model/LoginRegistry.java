package pl.empik.rest.userrestservice.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;


@Data
@Builder
@Entity(name = "LOGIN_REGISTRY")
@AllArgsConstructor
@NoArgsConstructor
public class LoginRegistry {
    @Id
    @Column(name = "LOGIN")
    private String login;
    @Column(name = "REQUEST_COUNT")
    private Long requestCount;
}

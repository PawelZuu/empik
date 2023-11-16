package pl.empik.rest.userrestservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.empik.rest.userrestservice.model.LoginRegistry;

public interface LoginRegistryRepository extends JpaRepository<LoginRegistry, String> {
}

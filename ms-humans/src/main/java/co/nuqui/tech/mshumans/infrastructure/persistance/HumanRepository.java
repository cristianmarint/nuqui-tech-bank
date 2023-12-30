package co.nuqui.tech.mshumans.infrastructure.persistance;

import co.nuqui.tech.mshumans.domain.dto.Human;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HumanRepository extends JpaRepository<Human, Long> {
    Human findByIdentification(String identification);
}

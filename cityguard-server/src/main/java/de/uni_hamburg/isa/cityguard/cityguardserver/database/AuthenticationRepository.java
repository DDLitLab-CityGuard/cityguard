package de.uni_hamburg.isa.cityguard.cityguardserver.database;

import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.AuthenticationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository extends JpaRepository<AuthenticationToken,Long> {
}

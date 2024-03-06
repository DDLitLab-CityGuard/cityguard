package de.uni_hamburg.isa.cityguard.cityguardserver.database;

import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.CgUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<CgUser, Long> {
	Optional<CgUser> findByEmail(String email);
	Boolean existsByEmail(String email);
}

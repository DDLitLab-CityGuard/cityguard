package de.uni_hamburg.isa.cityguard.cityguardserver.database;

import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.CgUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<CgUser, Long> {
	Optional<CgUser> findByEmail(String email);
	Optional<CgUser> findByUsernameOrEmail(String username, String email);
	Optional<CgUser> findByUsername(String username);
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
}

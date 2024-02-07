package de.uni_hamburg.isa.cityguard.cityguardserver.database;

import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.Nutzer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NutzerRepository extends JpaRepository<Nutzer, Long> {
	Optional<Nutzer> findByEmail(String email);
	Optional<Nutzer> findByUsernameOrEmail(String username, String email);
	Optional<Nutzer> findByUsername(String username);
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
}

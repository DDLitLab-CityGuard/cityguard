package de.uni_hamburg.isa.cityguard.cityguardserver.processing;

import de.uni_hamburg.isa.cityguard.cityguardserver.database.NutzerRepository;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.Nutzer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

	private final NutzerRepository nutzerRepository;


	public UserService(NutzerRepository nutzerRepository) {
		this.nutzerRepository = nutzerRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		Nutzer user = nutzerRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
				.orElseThrow(() ->
						new UsernameNotFoundException("User not found with username or email: "+ usernameOrEmail));

		Set<GrantedAuthority> authorities = user
				.getRoles()
				.stream()
				.map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

		return new org.springframework.security.core.userdetails.User(user.getEmail(),
				user.getPassword(),
				authorities);
	}
}

package de.uni_hamburg.isa.cityguard.cityguardserver.database.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@Entity
public class AuthenticationToken {

    @Id
    private Long id;
    private LocalDateTime expirationDate;
    @ManyToOne
    private CgUser cgUser;
}

package com.example.cityguardserver.database;

import com.example.cityguardserver.database.dto.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Report,Long> {
}

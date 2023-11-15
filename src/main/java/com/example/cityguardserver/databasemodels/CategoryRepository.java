package com.example.cityguardserver.databasemodels;

import com.example.cityguardserver.databasemodels.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Report,Long> {
}

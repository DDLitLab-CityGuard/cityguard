package com.example.cityguardserver.database;

import com.example.cityguardserver.database.dto.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    @NonNull
    Optional<Category> findById(@NonNull Long id);
}
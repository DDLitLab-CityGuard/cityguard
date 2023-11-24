package com.example.cityguardserver.database;

import com.example.cityguardserver.database.dto.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *  Repository for the Category Table in the Database.
 *  It is a Spring Data JPA repository, so it will automatically generate the
 *  implementation of the methods declared here.
 *  It is also a Spring component, so it will be automatically instantiated and
 *  injected in other components.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    /**
     *  Find the category with the given id.
     *  @param id the id of the category to find
     *  @return the category with the given id, or an empty optional if no category
     *          with the given id exists
     */
    @NonNull
    Optional<Category> findById(@NonNull Long id);
}

package com.example.cityguardserver.database;

import com.example.cityguardserver.database.dto.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long> {

    @Query(
            "SELECT r FROM Report r "
            + "WHERE ( (:minlo < :maxlo AND r.longitude > :minlo AND r.longitude < :maxlo) OR (:minlo > :maxlo AND (r.longitude > :minlo OR r.longitude < :maxlo)) ) "
            + "AND (r.latitude > :minlat AND r.latitude < :maxlat)"
    )
    List<Report> findBetweenBounds(
            @Param("minlo") Float minLongitude,
            @Param("maxlo") Float maxLongitude,
            @Param("minlat") Float minLatitude,
            @Param("maxlat") Float maxLatitude
    );

}

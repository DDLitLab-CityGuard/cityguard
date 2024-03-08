package de.uni_hamburg.isa.cityguard.cityguardserver.database;

import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.Category;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.CgUser;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


/**
 *  Repository for the Report Table in the Database.
 *  It is a Spring Data JPA repository, so it will automatically generate the
 *  implementation of the methods declared here.
 *  It is also a Spring component, so it will be automatically instantiated and
 *  injected in other components.
 */
@Repository
public interface ReportRepository extends JpaRepository<Report,Long> {

	/**
	 *  Find all the reports in the database that are between the given bounds.
	 *  The bounds are defined by the minimum and maximum longitude and latitude.
	 *  The longitude bounds can be inverted, so that the bounds can be defined
	 *  even if they cross the 180th meridian.
	 *  @param minLongitude the minimum longitude
	 *  @param maxLongitude the maximum longitude
	 *  @param minLatitude the minimum latitude
	 *  @param maxLatitude the maximum latitude
	 *  @return the list of reports that are between the given bounds
	 */
	@Query(
			"SELECT r FROM Report r "
			+ "WHERE ( (:minlo < :maxlo AND r.longitude > :minlo AND r.longitude < :maxlo) OR (:minlo > :maxlo AND (r.longitude > :minlo OR r.longitude < :maxlo)) ) "
			+ "AND (r.latitude > :minlat AND r.latitude < :maxlat)"
			+ "AND (r.category.id IN :categories)"
	)
	List<Report> findBetweenBounds(
			@Param("minlo") Float minLongitude,
			@Param("maxlo") Float maxLongitude,
			@Param("minlat") Float minLatitude,
			@Param("maxlat") Float maxLatitude,
			@Param("categories") List<Long> categories
	);




	@Query("SELECT r FROM Report r "
			+ "WHERE ( (:user = r.user) AND (:cat=r.category)) "
			+"AND(r.spam = false)"
			+ "AND (:datetimealtered < r.dateTime)"
			+"AND (:datetimeentered > r.dateTime)"	)
	List<Report> findSimilarReportsFromSameUser(
			@Param("user") CgUser user,
			@Param("cat") Category cat,
			@Param("datetimealtered")LocalDateTime dateTimeAltered,
			@Param("datetimeentered") LocalDateTime dateTime
			);

}

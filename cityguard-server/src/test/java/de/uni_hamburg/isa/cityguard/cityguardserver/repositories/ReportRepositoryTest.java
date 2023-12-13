package de.uni_hamburg.isa.cityguard.cityguardserver.repositories;


import de.uni_hamburg.isa.cityguard.cityguardserver.database.CategoryRepository;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.ReportRepository;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.Category;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.Report;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReportRepositoryTest {

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	public void ReportRepository_SaveReport_ReturnsReport() {
		// Arrange
		Category category1 = new Category();
		category1.setName("Test Category");
		category1.setAllowDiscrete(true);

		Report report1 = new Report();
		report1.setCategory(category1);
		report1.setDescription("Test Description");
		report1.setLatitude(1.0f);
		report1.setLongitude(1.0f);
		LocalDateTime dateTime = LocalDateTime.now();
		report1.setDateTime(dateTime);


		// Act
		Category savedCategory1 = categoryRepository.save(category1);
		Report savedReport1 = reportRepository.save(report1);

		// Assert
		Assertions.assertThat(savedReport1).isNotNull();
		Assertions.assertThat(savedReport1.getId()).isGreaterThan(0);
		Assertions.assertThat(savedReport1.getCategory()).isEqualTo(report1.getCategory());
		Assertions.assertThat(savedReport1.getDescription()).isEqualTo(report1.getDescription());
		Assertions.assertThat(savedReport1.getLatitude()).isEqualTo(report1.getLatitude());
		Assertions.assertThat(savedReport1.getLongitude()).isEqualTo(report1.getLongitude());
		Assertions.assertThat(savedReport1.getDateTime()).isEqualTo(dateTime);
	}
}

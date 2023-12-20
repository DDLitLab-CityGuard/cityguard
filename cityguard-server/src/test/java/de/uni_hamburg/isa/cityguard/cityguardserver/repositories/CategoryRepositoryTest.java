package de.uni_hamburg.isa.cityguard.cityguardserver.repositories;


import de.uni_hamburg.isa.cityguard.cityguardserver.database.CategoryRepository;
import de.uni_hamburg.isa.cityguard.cityguardserver.database.dto.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	public void CategoryRepository_SaveCategory_ReturnsCategory() {
		// Arrange
		Category category1 = new Category();
		category1.setName("Test Category");
		category1.setAllowDiscrete(true);

		Category category2 = new Category();
		category2.setName("Test Category 2");
		category2.setAllowDiscrete(false);

		// Act
		Category savedCategory1 = categoryRepository.save(category1);
		Category savedCategory2 = categoryRepository.save(category2);

		// Assert
		Assertions.assertThat(savedCategory1).isNotNull();
		Assertions.assertThat(savedCategory1.getId()).isGreaterThan(0);
		Assertions.assertThat(savedCategory1.getName()).isEqualTo(category1.getName());
		Assertions.assertThat(savedCategory1.getAllowDiscrete()).isEqualTo(category1.getAllowDiscrete());

		Assertions.assertThat(savedCategory2).isNotNull();
		Assertions.assertThat(savedCategory2.getId()).isGreaterThan(0);
		Assertions.assertThat(savedCategory2.getName()).isEqualTo(category2.getName());
		Assertions.assertThat(savedCategory2.getAllowDiscrete()).isEqualTo(category2.getAllowDiscrete());
	}
}

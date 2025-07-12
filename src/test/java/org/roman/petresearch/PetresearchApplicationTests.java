package org.roman.petresearch;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class PetresearchApplicationTests {

	@Test
	void contextLoads() {
	}

}

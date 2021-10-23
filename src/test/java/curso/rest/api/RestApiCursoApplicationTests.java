package curso.rest.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import curso.rest.api.config.ApiConfig;

@SpringBootTest
class RestApiCursoApplicationTests {

	@Autowired
	ApiConfig config;


	
	@Test
	void contextLoads() {
	}
	

}

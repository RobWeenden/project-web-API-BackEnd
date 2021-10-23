package curso.rest.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import curso.rest.api.config.ApiConfig;

@SpringBootApplication
@EntityScan(basePackages = { "curso.rest.api.model" })
@ComponentScan(basePackages = { "curso.rest" })
//@EnableJpaRepositories(basePackages = {"curso.rest.api.repository"})
@EnableTransactionManagement
@EnableWebMvc
@RestController
@EnableAutoConfiguration
@EnableCaching
@EnableConfigurationProperties(ApiConfig.class)
public class RestApiCursoApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(RestApiCursoApplication.class, args);

	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// WebMvcConfigurer.super.addCorsMappings(registry);
		registry.addMapping("/usuario/**").allowedMethods("*").allowedOrigins("*");

		registry.addMapping("/profissao/**").allowedMethods("*").allowedOrigins("*");

		registry.addMapping("/recuperar/**").allowedMethods("*").allowedOrigins("*");

	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("/login");
		registry.setOrder(Ordered.LOWEST_PRECEDENCE);

	}

}

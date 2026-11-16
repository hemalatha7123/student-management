package com.student.student_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenAPIConfig {

	@Bean
	public OpenAPI studentManagementAPI() {

		return new OpenAPI()
				.info(new Info()
						.title("Student Management API")
						.description("REST APIs for managing students and courses")
						.version("1.0.0"))
						.components(new Components()
								.addSecuritySchemes("basicAuth",
										new SecurityScheme()
										.type(SecurityScheme.Type.HTTP)
										.scheme("basic")))
						.addSecurityItem(
								new SecurityRequirement()
								.addList("basicAuth"));
	}
}

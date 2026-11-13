package com.student.student_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	 // Configure authentication and authorization
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
        	.cors(cors -> {})
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
            	//.requestMatchers("/auth/login").permitAll()
                .requestMatchers("/api/students/**")
                .authenticated()
                
                .requestMatchers("/api/courses/**")
                .authenticated()
                
                .requestMatchers("/api/enrollments/**")
                .authenticated()

                .requestMatchers("/h2-console/**")
                .permitAll()

                .anyRequest()
                .authenticated()
            )

            .httpBasic(basic -> { })
            /*.oauth2ResourceServer(oauth2 ->
	            oauth2.jwt(
	                org.springframework.security.config.Customizer
	                    .withDefaults()
	            )
	        )*/

            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }

    // Password encryption
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // Configure users
    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder) {

        UserDetails adminUser = User.builder()
            .username("admin")
            .password(passwordEncoder.encode("Admin@123"))
            .roles("ADMIN")
            .build();
        
        UserDetails normalUser = User.builder()
        		.username("user")
        		.password(passwordEncoder.encode("User@123"))
        		.roles("USER")
        		.build();

        return new InMemoryUserDetailsManager(
        		adminUser,normalUser);
    }

}

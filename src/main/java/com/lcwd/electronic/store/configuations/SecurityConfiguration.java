package com.lcwd.electronic.store.configuations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.lcwd.electronic.store.security.JwtAuthenticationEntryPoint;
import com.lcwd.electronic.store.security.JwtAuthenticationFilter;
import com.lcwd.electronic.store.services.Impl.CustomUserDetailsServiceImpl;

@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
	
	
	@Autowired
	private CustomUserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsServiceImpl);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	/*
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
//		Basic Authentication
		httpSecurity.csrf()
		.disable()
		.cors()
		.disable()
		.authorizeRequests()
		.requestMatchers(PUBLIC_URLS)
		.permitAll()
		.requestMatchers("/auth/login") // for Login the User
		.permitAll()
		.requestMatchers(HttpMethod.POST,"/users") //for Register the User
		.permitAll()
		.requestMatchers(HttpMethod.DELETE,"/users/**") // for delete the User
		.hasRole("ADMIN")
		.requestMatchers(HttpMethod.POST,"/categories") // for creating the categories
		.hasRole("ADMIN")
		.anyRequest()
		.authenticated()
		.and()
		.exceptionHandling()
		.authenticationEntryPoint(jwtAuthenticationEntryPoint)
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	
		return httpSecurity.build();
	}
*/
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
	    httpSecurity.csrf()
	        .disable()
	        .cors()
	        .disable()
	        .authorizeRequests()
	    	.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
					"/swagger-resources/**", "/webjars/**", "/v1/auth/**")
			.permitAll() // Permit all Swagger-related URLs
	        .requestMatchers("/auth/login").permitAll() 
	        .requestMatchers(HttpMethod.POST, "/users").permitAll()
	        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
	        .requestMatchers(HttpMethod.POST, "/categories").hasRole("ADMIN")
	        .anyRequest().authenticated()
	        .and()
	        .exceptionHandling()
	        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
	        .and()
	        .sessionManagement()
	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	    
	    httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	    return httpSecurity.build();
	}
	
	


	
}

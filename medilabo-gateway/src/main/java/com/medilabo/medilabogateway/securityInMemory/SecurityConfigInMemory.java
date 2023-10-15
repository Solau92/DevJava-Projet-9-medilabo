package com.medilabo.medilabogateway.securityInMemory;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring security configuration.
 * 
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfigInMemory {

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

		http
		.csrf(csrf -> csrf.disable())
		.authorizeExchange(exchanges -> exchanges.anyExchange().authenticated()).httpBasic(withDefaults())
		;
				
		return http.build();
	}

	@Bean
	public MapReactiveUserDetailsService  userDetailsService() {

		UserDetails user = User.builder().username("medilaboUser").password(passwordEncoder().encode("medilaboUserPassword"))
				.build();

		UserDetails admin = User.builder().username("medilaboAdmin").password(passwordEncoder().encode("medilaboAdminPassword"))
				.build();
		
		return new MapReactiveUserDetailsService(user, admin);
	}
	
}

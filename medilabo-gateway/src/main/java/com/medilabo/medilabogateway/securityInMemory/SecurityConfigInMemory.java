package com.medilabo.medilabogateway.securityInMemory;



import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler;

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
		.authorizeExchange(exchanges -> exchanges.anyExchange().authenticated()).httpBasic(withDefaults());
		return http.build();
	}

	@Bean
	public MapReactiveUserDetailsService  userDetailsService() {

		UserDetails user = User.builder().username("user").password(passwordEncoder().encode("password"))
				.build();

		UserDetails admin = User.builder().username("admin").password(passwordEncoder().encode("admin"))
				.build();
		return new MapReactiveUserDetailsService(user, admin);
	}
}

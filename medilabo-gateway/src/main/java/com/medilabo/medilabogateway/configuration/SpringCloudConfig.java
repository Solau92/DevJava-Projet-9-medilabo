package com.medilabo.medilabogateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration 
public class SpringCloudConfig {

	@Bean
	RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("r1", r -> r.host("r1")
						.and()
						.path("/path1")
						.uri("uri1"))
				.route("r2", r -> r.host("r2")
						.and()
						.path("/path2")
						.filters(f -> f.prefixPath("/prefix"))
						.uri("uri2"))
				.build();
	}
}

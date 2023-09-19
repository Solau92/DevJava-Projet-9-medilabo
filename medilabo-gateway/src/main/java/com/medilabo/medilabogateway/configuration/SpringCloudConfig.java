package com.medilabo.medilabogateway.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpringCloudConfig {
	
	private static final Logger log = LoggerFactory.getLogger(SpringCloudConfig.class);

	@Value("${microservice-gateway.uri}")
	private String msGatewayUri;

	@Value("${microservice-patient.uri}")
	private String msPatientUri;

	@Bean
	RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
		
		log.info("read properties: " + msPatientUri + " " + msGatewayUri);
		
		return builder.routes()
				.route(r -> r.path("/ms-patient/patient/**")
						.filters(f -> f.rewritePath("/ms-patient/patient/(?<segment>.*)", "/patient/${segment}"))
						.uri(msPatientUri))
				.route(r -> r.path("/auth/login")
						.filters(f -> f.rewritePath("/auth/login", "/login"))
						.uri(msGatewayUri))
				.build();	
	}

//	@Bean
//	RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
//		return builder.routes()
//				.route(r -> r.path("/patient/**")
//						.filters(f -> f.filter(new LoggerFilter()))
//                        .uri("http://localhost:8081/"))
//				
//                .build();
//	}

//	@Bean
//	RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
//		return builder.routes()
//				.route("r1", r -> r.host("r1")
//						.and()
//						.path("/path1")
//						.uri("uri1"))
//				.route("r2", r -> r.host("r2")
//						.and()
//						.path("/path2")
//						.filters(f -> f.prefixPath("/prefix"))
//						.uri("uri2"))
//				.build();
//	}
}

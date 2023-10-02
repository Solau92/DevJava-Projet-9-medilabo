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
	
	@Value("${microservice-note.uri}")
	private String msNoteUri;
	
	@Value("${microservice-risk.uri}")
	private String msRiskUri;

	@Bean
	RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
				
		return builder.routes()
				.route(r -> r.path("/ms-patient/patient/**")
						.filters(f -> f.rewritePath("/ms-patient/patient/(?<segment>.*)", "/patient/${segment}"))
						.uri(msPatientUri))
				.route(r -> r.path("/auth/login")
						.filters(f -> f.rewritePath("/auth/login", "/login"))
						.uri(msGatewayUri))
				.route(r -> r.path("/ms-note/note/**")
						.filters(f -> f.rewritePath("/ms-note/note/(?<segment>.*)", "/note/${segment}"))
						.uri(msNoteUri))
				.route(r -> r.path("/ms-risk/risk/**")
						.filters(f -> f.rewritePath("/ms-risk/risk/(?<segment>.*)", "/risk/${segment}"))
						.uri(msRiskUri))
				.build();	
	}

}

package com.directors.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Configuration
public class QuerydslConfig {
	@PersistenceContext
	private EntityManager em;

	@Bean
	JPAQueryFactory jpaQueryFactory() {
		return new JPAQueryFactory(em);
	}
}

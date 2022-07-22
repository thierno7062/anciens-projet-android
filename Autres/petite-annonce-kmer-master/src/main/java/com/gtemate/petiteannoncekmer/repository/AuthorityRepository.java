package com.gtemate.petiteannoncekmer.repository;

import com.gtemate.petiteannoncekmer.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}

package com.gtemate.petiteannoncekmer.repository;

import com.gtemate.petiteannoncekmer.domain.Localisation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Localisation entity.
 */
@SuppressWarnings("unused")
public interface LocalisationRepository extends JpaRepository<Localisation,Long>,JpaSpecificationExecutor<Localisation> {

    @Query("select localisation from Localisation localisation where localisation.user.login = ?#{principal.username}")
    List<Localisation> findByUserIsCurrentUser();

}

package com.gtemate.petiteannoncekmer.repository;

import com.gtemate.petiteannoncekmer.domain.Declaration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Declaration entity.
 */
@SuppressWarnings("unused")
public interface DeclarationRepository extends JpaRepository<Declaration,Long>,JpaSpecificationExecutor<Declaration> {

    @Query("select declaration from Declaration declaration where declaration.owner.login = ?#{principal.username} and" +
        " declaration.isPublished = true")
    Page<Declaration> findByOwnerIsCurrentUser(Pageable pageable);

    @Query("select declaration from Declaration declaration where (" +
        "declaration.localisation.region.code = :idRegion and " +
        "declaration.title LIKE CONCAT('%',:search,'%') and "+
        "declaration.isPublished = true) or " +
        "(declaration.localisation.region.code = :idRegion and " +
        "declaration.description LIKE CONCAT('%',:search,'%') and "+
        "declaration.isPublished = true"+
        ")")
    Page<Declaration> findAllDeclarationsByRegion(Pageable pageable,@Param("idRegion") String idRegion,@Param("search") String search);
}

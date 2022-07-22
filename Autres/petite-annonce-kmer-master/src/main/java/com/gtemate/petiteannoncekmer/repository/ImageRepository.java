package com.gtemate.petiteannoncekmer.repository;

import com.gtemate.petiteannoncekmer.domain.Image;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Image entity.
 */

public interface ImageRepository extends JpaRepository<Image,Long>,JpaSpecificationExecutor<Image> {

    @Query("from Image as img "
        + "where img.declaration.id = :declarationId "
        )
    List<Image> findByDeclaration(@Param("declarationId") Long declarationId);
}

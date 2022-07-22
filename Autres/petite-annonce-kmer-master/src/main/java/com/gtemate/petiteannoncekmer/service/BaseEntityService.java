package com.gtemate.petiteannoncekmer.service;

/**
 * Created by admin on 10/12/2016.
 */

import com.gtemate.petiteannoncekmer.domain.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * REST controller for managing entities.
 */
public abstract class BaseEntityService<E extends BaseEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseEntityService.class);

    public abstract JpaRepository<E, Long> getRepository();

    public JpaSpecificationExecutor<E> getPagineableRepository() {
        return null;
    }

    /**
     * Save an entity.
     *
     * @param entity
     *            the entity to save
     * @return the persisted entity
     */
    public E save(E entity) {
        LOGGER.debug("Request to save entity : {}", entity);
        E result = getRepository().save(entity);
        return result;
    }

    /**
     * Get all the entities.
     *
     * @return the list of entities
     */
    public List<E> findAll() {
        LOGGER.debug("Request to get all entities");

        List<E> result = getRepository().findAll();
        return result;
    }

    public List<E> findAll(String sortProperty) {
        LOGGER.debug("Request to get all entities sorted by {}", sortProperty);

        Sort s = new Sort(Sort.Direction.ASC, sortProperty);
        List<E> result = getRepository().findAll(s);
        return result;
    }

    /**
     * Get one entity by id.
     *
     * @param id
     *            the id of the entity
     * @return the entity
     */
    public E findOne(Long id) {
        LOGGER.debug("Request to get entity : {}", id);
        E result = getRepository().findOne(id);
        return result;
    }

    /**
     * Delete the entity by id.
     *
     * @param id
     *            the id of the entity
     */
    public void delete(Long id) {
        LOGGER.debug("Request to delete entity : {}", id);
        getRepository().delete(id);
    }

    /**
     * Delete all entities.
     */
    public void deleteAll() {
        LOGGER.debug("Request to delete all entities");
        getRepository().deleteAll();
    }


}


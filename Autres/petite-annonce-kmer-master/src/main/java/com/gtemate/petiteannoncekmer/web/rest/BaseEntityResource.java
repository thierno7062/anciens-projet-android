package com.gtemate.petiteannoncekmer.web.rest;

import com.gtemate.petiteannoncekmer.domain.BaseEntity;
import com.gtemate.petiteannoncekmer.service.BaseEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by admin on 10/12/2016.
 */

public abstract class BaseEntityResource<E extends BaseEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseEntityService.class);

    public abstract BaseEntityService<E> getEntityService();
}

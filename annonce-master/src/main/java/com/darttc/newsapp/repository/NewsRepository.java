package com.darttc.newsapp.repository;

import com.darttc.newsapp.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the News entity.
 */
public interface NewsRepository extends JpaRepository<News,Long>{

}

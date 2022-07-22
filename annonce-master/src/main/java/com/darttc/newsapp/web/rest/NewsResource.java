package com.darttc.newsapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.darttc.newsapp.domain.News;
import com.darttc.newsapp.repository.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing News.
 */
@RestController
@RequestMapping("/api")
public class NewsResource {

    private final Logger log = LoggerFactory.getLogger(NewsResource.class);

    @Inject
    private NewsRepository newsRepository;

    /**
     * POST  /newss -> Create a new news.
     */
    @RequestMapping(value = "/newss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody News news) {
        log.debug("REST request to save News : {}", news);
        newsRepository.save(news);
    }

    /**
     * GET  /newss -> get all the newss.
     */
    @RequestMapping(value = "/newss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<News> getAll() {
        log.debug("REST request to get all Newss");
        return newsRepository.findAll();
    }

    /**
     * GET  /newss/:id -> get the "id" news.
     */
    @RequestMapping(value = "/newss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<News> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get News : {}", id);
        News news = newsRepository.findOne(id);
        if (news == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    /**
     * DELETE  /newss/:id -> delete the "id" news.
     */
    @RequestMapping(value = "/newss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete News : {}", id);
        newsRepository.delete(id);
    }
}

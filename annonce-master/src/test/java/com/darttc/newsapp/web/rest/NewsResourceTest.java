package com.darttc.newsapp.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import com.darttc.newsapp.Application;
import com.darttc.newsapp.domain.News;
import com.darttc.newsapp.repository.NewsRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the NewsResource REST controller.
 *
 * @see NewsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class NewsResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_SUBJECT = "SAMPLE_TEXT";
    private static final String UPDATED_SUBJECT = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";
    private static final String DEFAULT_AUTHOR = "SAMPLE_TEXT";
    private static final String UPDATED_AUTHOR = "UPDATED_TEXT";

    private static final DateTime DEFAULT_CREATION_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATION_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATION_DATE_STR = dateTimeFormatter.print(DEFAULT_CREATION_DATE);
    private static final String DEFAULT_IMAGE = "SAMPLE_TEXT";
    private static final String UPDATED_IMAGE = "UPDATED_TEXT";

    @Inject
    private NewsRepository newsRepository;

    private MockMvc restNewsMockMvc;

    private News news;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NewsResource newsResource = new NewsResource();
        ReflectionTestUtils.setField(newsResource, "newsRepository", newsRepository);
        this.restNewsMockMvc = MockMvcBuilders.standaloneSetup(newsResource).build();
    }

    @Before
    public void initTest() {
        news = new News();
        news.setSubject(DEFAULT_SUBJECT);
        news.setDescription(DEFAULT_DESCRIPTION);
        news.setAuthor(DEFAULT_AUTHOR);
        news.setCreationDate(DEFAULT_CREATION_DATE);
        news.setImage(DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    public void createNews() throws Exception {
        // Validate the database is empty
        assertThat(newsRepository.findAll()).hasSize(0);

        // Create the News
        restNewsMockMvc.perform(post("/api/newss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(news)))
                .andExpect(status().isOk());

        // Validate the News in the database
        List<News> newss = newsRepository.findAll();
        assertThat(newss).hasSize(1);
        News testNews = newss.iterator().next();
        assertThat(testNews.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testNews.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testNews.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testNews.getCreationDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testNews.getImage()).isEqualTo(DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    public void getAllNewss() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newss
        restNewsMockMvc.perform(get("/api/newss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(news.getId().intValue()))
                .andExpect(jsonPath("$.[0].subject").value(DEFAULT_SUBJECT.toString()))
                .andExpect(jsonPath("$.[0].description").value(DEFAULT_DESCRIPTION.toString()))
                .andExpect(jsonPath("$.[0].author").value(DEFAULT_AUTHOR.toString()))
                .andExpect(jsonPath("$.[0].creationDate").value(DEFAULT_CREATION_DATE_STR))
                .andExpect(jsonPath("$.[0].image").value(DEFAULT_IMAGE.toString()));
    }

    @Test
    @Transactional
    public void getNews() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get the news
        restNewsMockMvc.perform(get("/api/newss/{id}", news.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(news.getId().intValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE_STR))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNews() throws Exception {
        // Get the news
        restNewsMockMvc.perform(get("/api/newss/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNews() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Update the news
        news.setSubject(UPDATED_SUBJECT);
        news.setDescription(UPDATED_DESCRIPTION);
        news.setAuthor(UPDATED_AUTHOR);
        news.setCreationDate(UPDATED_CREATION_DATE);
        news.setImage(UPDATED_IMAGE);
        restNewsMockMvc.perform(post("/api/newss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(news)))
                .andExpect(status().isOk());

        // Validate the News in the database
        List<News> newss = newsRepository.findAll();
        assertThat(newss).hasSize(1);
        News testNews = newss.iterator().next();
        assertThat(testNews.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testNews.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testNews.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testNews.getCreationDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testNews.getImage()).isEqualTo(UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void deleteNews() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get the news
        restNewsMockMvc.perform(delete("/api/newss/{id}", news.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<News> newss = newsRepository.findAll();
        assertThat(newss).hasSize(0);
    }
}

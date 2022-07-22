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
import java.util.List;

import com.darttc.newsapp.Application;
import com.darttc.newsapp.domain.Author;
import com.darttc.newsapp.repository.AuthorRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AuthorResource REST controller.
 *
 * @see AuthorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AuthorResourceTest {

    private static final String DEFAULT_FIRST_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_FIRST_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_LAST_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_LAST_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_PSEUDO = "SAMPLE_TEXT";
    private static final String UPDATED_PSEUDO = "UPDATED_TEXT";
    private static final String DEFAULT_EMAIL = "SAMPLE_TEXT";
    private static final String UPDATED_EMAIL = "UPDATED_TEXT";
    private static final String DEFAULT_PASSWORD = "SAMPLE_TEXT";
    private static final String UPDATED_PASSWORD = "UPDATED_TEXT";
    private static final String DEFAULT_PICTURE = "SAMPLE_TEXT";
    private static final String UPDATED_PICTURE = "UPDATED_TEXT";

    @Inject
    private AuthorRepository authorRepository;

    private MockMvc restAuthorMockMvc;

    private Author author;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AuthorResource authorResource = new AuthorResource();
        ReflectionTestUtils.setField(authorResource, "authorRepository", authorRepository);
        this.restAuthorMockMvc = MockMvcBuilders.standaloneSetup(authorResource).build();
    }

    @Before
    public void initTest() {
        author = new Author();
        author.setFirstName(DEFAULT_FIRST_NAME);
        author.setLastName(DEFAULT_LAST_NAME);
        author.setPseudo(DEFAULT_PSEUDO);
        author.setEmail(DEFAULT_EMAIL);
        author.setPassword(DEFAULT_PASSWORD);
        author.setPicture(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    public void createAuthor() throws Exception {
        // Validate the database is empty
        assertThat(authorRepository.findAll()).hasSize(0);

        // Create the Author
        restAuthorMockMvc.perform(post("/api/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(author)))
                .andExpect(status().isOk());

        // Validate the Author in the database
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(1);
        Author testAuthor = authors.iterator().next();
        assertThat(testAuthor.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAuthor.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAuthor.getPseudo()).isEqualTo(DEFAULT_PSEUDO);
        assertThat(testAuthor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAuthor.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testAuthor.getPicture()).isEqualTo(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    public void getAllAuthors() throws Exception {
        // Initialize the database
        authorRepository.saveAndFlush(author);

        // Get all the authors
        restAuthorMockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(author.getId().intValue()))
                .andExpect(jsonPath("$.[0].firstName").value(DEFAULT_FIRST_NAME.toString()))
                .andExpect(jsonPath("$.[0].lastName").value(DEFAULT_LAST_NAME.toString()))
                .andExpect(jsonPath("$.[0].pseudo").value(DEFAULT_PSEUDO.toString()))
                .andExpect(jsonPath("$.[0].email").value(DEFAULT_EMAIL.toString()))
                .andExpect(jsonPath("$.[0].password").value(DEFAULT_PASSWORD.toString()))
                .andExpect(jsonPath("$.[0].picture").value(DEFAULT_PICTURE.toString()));
    }

    @Test
    @Transactional
    public void getAuthor() throws Exception {
        // Initialize the database
        authorRepository.saveAndFlush(author);

        // Get the author
        restAuthorMockMvc.perform(get("/api/authors/{id}", author.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(author.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.pseudo").value(DEFAULT_PSEUDO.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAuthor() throws Exception {
        // Get the author
        restAuthorMockMvc.perform(get("/api/authors/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuthor() throws Exception {
        // Initialize the database
        authorRepository.saveAndFlush(author);

        // Update the author
        author.setFirstName(UPDATED_FIRST_NAME);
        author.setLastName(UPDATED_LAST_NAME);
        author.setPseudo(UPDATED_PSEUDO);
        author.setEmail(UPDATED_EMAIL);
        author.setPassword(UPDATED_PASSWORD);
        author.setPicture(UPDATED_PICTURE);
        restAuthorMockMvc.perform(post("/api/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(author)))
                .andExpect(status().isOk());

        // Validate the Author in the database
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(1);
        Author testAuthor = authors.iterator().next();
        assertThat(testAuthor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAuthor.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAuthor.getPseudo()).isEqualTo(UPDATED_PSEUDO);
        assertThat(testAuthor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAuthor.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testAuthor.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    public void deleteAuthor() throws Exception {
        // Initialize the database
        authorRepository.saveAndFlush(author);

        // Get the author
        restAuthorMockMvc.perform(delete("/api/authors/{id}", author.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(0);
    }
}

package com.gtemate.petiteannoncekmer.web.rest;

import com.gtemate.petiteannoncekmer.PetiteAnnonceKmerApp;

import com.gtemate.petiteannoncekmer.domain.Declaration;
import com.gtemate.petiteannoncekmer.domain.User;
import com.gtemate.petiteannoncekmer.repository.DeclarationRepository;
import com.gtemate.petiteannoncekmer.repository.UserRepository;
import com.gtemate.petiteannoncekmer.service.DeclarationService;

import com.gtemate.petiteannoncekmer.service.MailService;
import com.gtemate.petiteannoncekmer.service.UserService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static com.gtemate.petiteannoncekmer.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DeclarationResource REST controller.
 *
 * @see DeclarationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PetiteAnnonceKmerApp.class)
public class DeclarationResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_IS_PUBLISHED = false;
    private static final Boolean UPDATED_IS_PUBLISHED = true;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final ZonedDateTime DEFAULT_PUBLISHED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PUBLISHED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Inject
    private DeclarationRepository declarationRepository;

    @Inject
    private DeclarationService declarationService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDeclarationMockMvc;

    private Declaration declaration;



    @Inject
    private UserService userService;

    @Mock
    private UserService mockUserService;

    @Mock
    private MailService mockMailService;

    @Inject
    private UserRepository userRepository;

    private MockMvc restUserMockMvc;

    private MockMvc restMvc;



    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        doNothing().when(mockMailService).sendDeclarationIsPublishedMail((Declaration) anyObject());
        DeclarationResource declarationResource = new DeclarationResource();
        ReflectionTestUtils.setField(declarationResource, "declarationService", declarationService);
        ReflectionTestUtils.setField(declarationResource, "userService", userService);
        ReflectionTestUtils.setField(declarationResource, "mailService", mockMailService);
        this.restDeclarationMockMvc = MockMvcBuilders.standaloneSetup(declarationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();

        /*User auth test */
        AccountResource accountResource = new AccountResource();
        ReflectionTestUtils.setField(accountResource, "userRepository", userRepository);
        ReflectionTestUtils.setField(accountResource, "userService", userService);
        ReflectionTestUtils.setField(accountResource, "mailService", mockMailService);

        AccountResource accountUserMockResource = new AccountResource();
        ReflectionTestUtils.setField(accountUserMockResource, "userRepository", userRepository);
        ReflectionTestUtils.setField(accountUserMockResource, "userService", mockUserService);
        ReflectionTestUtils.setField(accountUserMockResource, "mailService", mockMailService);

        this.restMvc = MockMvcBuilders.standaloneSetup(accountResource).build();
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(accountUserMockResource).build();



    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Declaration createEntity(EntityManager em) {
        Declaration declaration = new Declaration()
                .title(DEFAULT_TITLE)
                .description(DEFAULT_DESCRIPTION)
                .creationDate(DEFAULT_CREATION_DATE)
                .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
                .isPublished(DEFAULT_IS_PUBLISHED)
                .price(DEFAULT_PRICE)
                .publishedDate(DEFAULT_PUBLISHED_DATE);
        return declaration;
    }

    @Before
    public void initTest() {
        declaration = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeclaration() throws Exception {
        int databaseSizeBeforeCreate = declarationRepository.findAll().size();

        restUserMockMvc.perform(get("/api/authenticate")
            .with(request -> {
                request.setRemoteUser("test");
                return request;
            })
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("test"));

        // Create the Declaration

        restDeclarationMockMvc.perform(post("/api/declarations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(declaration)))
            .andExpect(status().isCreated());

        // Validate the Declaration in the database
        List<Declaration> declarations = declarationRepository.findAll();
        assertThat(declarations).hasSize(databaseSizeBeforeCreate + 1);
        Declaration testDeclaration = declarations.get(declarations.size() - 1);
        assertThat(testDeclaration.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDeclaration.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDeclaration.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testDeclaration.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testDeclaration.isIsPublished()).isEqualTo(DEFAULT_IS_PUBLISHED);
        assertThat(testDeclaration.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testDeclaration.getPublishedDate()).isEqualTo(DEFAULT_PUBLISHED_DATE);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = declarationRepository.findAll().size();
        // set the field null
        declaration.setTitle(null);

        // Create the Declaration, which fails.

        restDeclarationMockMvc.perform(post("/api/declarations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(declaration)))
            .andExpect(status().isBadRequest());

        List<Declaration> declarations = declarationRepository.findAll();
        assertThat(declarations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = declarationRepository.findAll().size();
        // set the field null
        declaration.setCreationDate(null);

        // Create the Declaration, which fails.

        restDeclarationMockMvc.perform(post("/api/declarations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(declaration)))
            .andExpect(status().isBadRequest());

        List<Declaration> declarations = declarationRepository.findAll();
        assertThat(declarations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastModifiedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = declarationRepository.findAll().size();
        // set the field null
        declaration.setLastModifiedDate(null);

        // Create the Declaration, which fails.

        restDeclarationMockMvc.perform(post("/api/declarations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(declaration)))
            .andExpect(status().isBadRequest());

        List<Declaration> declarations = declarationRepository.findAll();
        assertThat(declarations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsPublishedIsRequired() throws Exception {
        int databaseSizeBeforeTest = declarationRepository.findAll().size();
        // set the field null
        declaration.setIsPublished(null);

        // Create the Declaration, which fails.

        restDeclarationMockMvc.perform(post("/api/declarations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(declaration)))
            .andExpect(status().isBadRequest());

        List<Declaration> declarations = declarationRepository.findAll();
        assertThat(declarations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = declarationRepository.findAll().size();
        // set the field null
        declaration.setPrice(null);

        // Create the Declaration, which fails.

        restDeclarationMockMvc.perform(post("/api/declarations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(declaration)))
            .andExpect(status().isBadRequest());

        List<Declaration> declarations = declarationRepository.findAll();
        assertThat(declarations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    @Ignore
    public void getAllDeclarations() throws Exception {
        // Initialize the database
        declarationRepository.saveAndFlush(declaration);

        // Get all the declarations
        restDeclarationMockMvc.perform(get("/api/declarations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(declaration.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].isPublished").value(hasItem(DEFAULT_IS_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].publishedDate").value(hasItem(sameInstant(DEFAULT_PUBLISHED_DATE))));
    }

    @Test
    @Transactional
    public void getDeclaration() throws Exception {
        // Initialize the database
        declarationRepository.saveAndFlush(declaration);

        // Get the declaration
        restDeclarationMockMvc.perform(get("/api/declarations/{id}", declaration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(declaration.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.lastModifiedDate").value(sameInstant(DEFAULT_LAST_MODIFIED_DATE)))
            .andExpect(jsonPath("$.isPublished").value(DEFAULT_IS_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.publishedDate").value(sameInstant(DEFAULT_PUBLISHED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingDeclaration() throws Exception {
        // Get the declaration
        restDeclarationMockMvc.perform(get("/api/declarations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeclaration() throws Exception {
        // Initialize the database
        declarationService.save(declaration);

        int databaseSizeBeforeUpdate = declarationRepository.findAll().size();

        // Update the declaration
        Declaration updatedDeclaration = declarationRepository.findOne(declaration.getId());
        updatedDeclaration
                .title(UPDATED_TITLE)
                .description(UPDATED_DESCRIPTION)
                .creationDate(UPDATED_CREATION_DATE)
                .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
                .isPublished(UPDATED_IS_PUBLISHED)
                .price(UPDATED_PRICE)
                .publishedDate(UPDATED_PUBLISHED_DATE);

        restDeclarationMockMvc.perform(put("/api/declarations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDeclaration)))
            .andExpect(status().isOk());

        // Validate the Declaration in the database
        List<Declaration> declarations = declarationRepository.findAll();
        assertThat(declarations).hasSize(databaseSizeBeforeUpdate);
        Declaration testDeclaration = declarations.get(declarations.size() - 1);
        assertThat(testDeclaration.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDeclaration.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDeclaration.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        //assertThat(testDeclaration.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testDeclaration.isIsPublished()).isEqualTo(UPDATED_IS_PUBLISHED);
        assertThat(testDeclaration.getPrice()).isEqualTo(UPDATED_PRICE);
        //assertThat(testDeclaration.getPublishedDate()).isEqualTo(UPDATED_PUBLISHED_DATE);
    }

    @Test
    @Transactional
    public void deleteDeclaration() throws Exception {
        // Initialize the database
        declarationService.save(declaration);

        int databaseSizeBeforeDelete = declarationRepository.findAll().size();

        // Get the declaration
        restDeclarationMockMvc.perform(delete("/api/declarations/{id}", declaration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Declaration> declarations = declarationRepository.findAll();
        assertThat(declarations).hasSize(databaseSizeBeforeDelete - 1);
    }
}

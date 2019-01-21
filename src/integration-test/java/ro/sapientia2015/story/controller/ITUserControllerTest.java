package ro.sapientia2015.story.controller;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import ro.sapientia2015.common.controller.ErrorController;
import ro.sapientia2015.config.ExampleApplicationContext;
import ro.sapientia2015.context.WebContextLoader;
import ro.sapientia2015.story.UserTestUtil;
import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("userData.xml")
public class ITUserControllerTest {
	private static final String EXPECTED_DB = "userData.xml";

    private static final String FORM_FIELD_BIO = "bio";
    private static final String FORM_FIELD_ID = "id";
    private static final String FORM_FIELD_NAME = "name";

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext)
                .build();
    }
    
    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value=EXPECTED_DB)
    public void showAddForm() throws Exception {
        mockMvc.perform(get("/users/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users/add.jsp"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("bio", isEmptyOrNullString())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("name", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value=EXPECTED_DB)
    public void addEmpty() throws Exception {
        mockMvc.perform(post("/users/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(UserController.MODEL_ATTRIBUTE, new UserDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("bio", isEmptyOrNullString())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("name", isEmptyOrNullString())));
    }
    
    @Test
    @ExpectedDatabase(value=EXPECTED_DB, assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void addWhenNameAndBioAreTooLong() throws Exception {
        String name = UserTestUtil.createStringWithLength(User.MAX_LENGTH_NAME + 1);
        String bio = UserTestUtil.createStringWithLength(User.MAX_LENGTH_BIO + 1);

        mockMvc.perform(post("/users/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_BIO, bio)
                .param(FORM_FIELD_NAME, name)
                .sessionAttr(UserController.MODEL_ATTRIBUTE, new UserDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE, "bio"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("bio", is(bio))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("name", is(name))));
    }

    @Test
    @ExpectedDatabase(value="userData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add() throws Exception {
        String expectedRedirectViewPath = UserTestUtil.createRedirectViewPath(UserController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/users/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_BIO, "bio")
                .param(FORM_FIELD_NAME, "name")
                .sessionAttr(UserController.MODEL_ATTRIBUTE, new UserDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(UserController.PARAMETER_ID, is("4")))
                .andExpect(flash().attribute(UserController.FLASH_MESSAGE_KEY_FEEDBACK, is("User: name was added.")));
    }
    
    @Test
    @ExpectedDatabase(value=EXPECTED_DB, assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void findAll() throws Exception {
        mockMvc.perform(get("/users/"))
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_LIST))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users/list.jsp"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_LIST, hasSize(3)))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("name", is("Gyula")),
                                hasProperty("bio", is("I am Gyula."))
                        )
                )))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("name", is("Jakab")),
                                hasProperty("bio", is("I am Jakab."))
                        )
                )))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(3L)),
                                hasProperty("name", is("Juci")),
                                hasProperty("bio", is("I am Juci."))
                        )
                )));
    }

    @Test
    @ExpectedDatabase(value=EXPECTED_DB, assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void findById() throws Exception {
        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_VIEW))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users/view.jsp"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("name", is("Gyula"))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("bio", is("I am Gyula."))));
    }

    @Test
    @ExpectedDatabase(value=EXPECTED_DB, assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void findByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/users/{id}", 4L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase(value="userData-delete-expected.xml", assertionMode=DatabaseAssertionMode.NON_STRICT)
    public void deleteById() throws Exception {
        String expectedRedirectViewPath = UserTestUtil.createRedirectViewPath(UserController.REQUEST_MAPPING_LIST);
        mockMvc.perform(get("/users/delete/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(flash().attribute(UserController.FLASH_MESSAGE_KEY_FEEDBACK, is("User: Gyula was deleted.")));
    }
    
    @Test
    @ExpectedDatabase(value=EXPECTED_DB, assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void deleteByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/users/delete/{id}", 4L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase(value=EXPECTED_DB, assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void showUpdateForm() throws Exception {
        mockMvc.perform(get("/users/update/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users/update.jsp"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("bio", is("I am Gyula."))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("name", is("Gyula"))));
    }

    @Test
    @ExpectedDatabase(value=EXPECTED_DB, assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void showUpdateFormWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/users/update/{id}", 4L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase(value=EXPECTED_DB, assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void updateEmpty() throws Exception {
        mockMvc.perform(post("/users/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_ID, "1")
                .sessionAttr(UserController.MODEL_ATTRIBUTE, new UserDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users/update.jsp"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("bio", isEmptyOrNullString())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("name", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase(value=EXPECTED_DB, assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void updateWhenNameAndBioAreTooLong() throws Exception {
        String name = UserTestUtil.createStringWithLength(User.MAX_LENGTH_NAME + 1);
        String bio = UserTestUtil.createStringWithLength(User.MAX_LENGTH_BIO + 1);

        mockMvc.perform(post("/users/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_BIO, bio)
                .param(FORM_FIELD_ID, "1")
                .param(FORM_FIELD_NAME, name)
                .sessionAttr(UserController.MODEL_ATTRIBUTE, new UserDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users/update.jsp"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE, "bio"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("bio", is(bio))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("name", is(name))));
    }
    
    @Test
    @ExpectedDatabase(value="userData-update-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void update() throws Exception {
        String expectedRedirectViewPath = UserTestUtil.createRedirectViewPath(UserController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/users/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_BIO, "bio")
                .param(FORM_FIELD_ID, "1")
                .param(FORM_FIELD_NAME, "name")
                .sessionAttr(UserController.MODEL_ATTRIBUTE, new UserDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(UserController.PARAMETER_ID, is("1")))
                .andExpect(flash().attribute(UserController.FLASH_MESSAGE_KEY_FEEDBACK, is("User: name was updated.")));
    }

    @Test
    @ExpectedDatabase(value=EXPECTED_DB, assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void updateWhenIsNotFound() throws Exception {
        mockMvc.perform(post("/users/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_BIO, "bio")
                .param(FORM_FIELD_ID, "4")
                .param(FORM_FIELD_NAME, "name")
                .sessionAttr(UserController.MODEL_ATTRIBUTE, new UserDTO())
        )
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

	
}

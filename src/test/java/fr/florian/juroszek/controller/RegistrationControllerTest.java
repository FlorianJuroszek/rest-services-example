package fr.florian.juroszek.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.florian.juroszek.Repository.UserRepository;
import fr.florian.juroszek.model.User;
import fr.florian.juroszek.utils.Country;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {

    @Autowired
    private RegistrationController controller;

    @Autowired
    private UserRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private User user;

    @Before
    public void init() throws Exception {
        repository.deleteAll();
        user = new User("florian.juroszek@gmail.com", "juroszek", "",
                "florian", 22, Country.FRANCE);
    }

    @Test
    @DirtiesContext
    public void contextLoads() throws Exception {
        Assert.assertNotNull(controller);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DirtiesContext
    public void createUserWithParams() throws Exception {
        String email = "florian.juroszek@gmail.com";

        // Mocking post request
        this.mockMvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("email", email)
                .param("firstName", "florian")
                .param("middleName", "")
                .param("lastName", "juroszek")
                .param("age", "22")
                .param("country", "FRANCE"))
                .andExpect(status().isOk())
                .andReturn();

        // Mapping the json returned value
        User userDb = this.repository.findByEmail(email);
        Assert.assertEquals("Unexpected retrieval", user, userDb);
    }

    @Test
    @DirtiesContext
    public void createUserUnder18() throws Exception {
        // Mocking post request
        this.mockMvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("email", "florian.juroszek@gmail.com")
                .param("firstName", "florian")
                .param("middleName", "")
                .param("lastName", "juroszek")
                .param("age", "17")
                .param("country", "FRANCE"))
                .andExpect(status().isForbidden())
                .andReturn();

        // Assert that there was no registration
        User userDb = this.repository.findByEmail("florian.juroszek@gmail.com");
        Assert.assertNull("Unexpected retrieval", userDb);
    }

    @Test
    @DirtiesContext
    public void createUserNotLivingInFrance() throws Exception {
        // Mocking post request
        this.mockMvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("email", "florian.juroszek@gmail.com")
                .param("firstName", "florian")
                .param("middleName", "")
                .param("lastName", "juroszek")
                .param("age", "19")
                .param("country", "GERMANY"))
                .andExpect(status().isForbidden())
                .andReturn();

        // Assert that there was no registration
        User userDb = this.repository.findByEmail("florian.juroszek@gmail.com");
        Assert.assertNull("Unexpected retrieval", userDb);
    }

    @Test
    @DirtiesContext
    public void createUserWithoutMiddleName() throws Exception {
        // Mocking post request
        this.mockMvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("email", "florian.juroszek@gmail.com")
                .param("firstName", "florian")
                .param("lastName", "juroszek")
                .param("age", "19")
                .param("country", "GERMANY"))
                .andExpect(status().isForbidden())
                .andReturn();

        // Assert that there was no registration
        User userDb = this.repository.findByEmail("florian.juroszek@gmail.com");
        Assert.assertNull("Unexpected retrieval", userDb);
    }

    @Test
    @DirtiesContext
    public void retrieveUser() throws Exception {
        // Testing NotFound response
        this.mockMvc.perform(get("/user/florian.juroszek@gmail.com")).andExpect(status().isNotFound());

        // Saving a user manually
        repository.save(user);

        // Mocking the request
        MvcResult result = this.mockMvc.perform(get("/user/" + this.user.getEmail())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Mapping the json returned value
        String json = result.getResponse().getContentAsString();
        User userDb = mapper.readValue(json, User.class);
        Assert.assertEquals("Unexpected retrieval", user, userDb);
    }

}

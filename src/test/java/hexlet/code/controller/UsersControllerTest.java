package hexlet.code.controller;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTest {
    private final String URL = "/api/users";
    private User testUser;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        testUser = new User();
        testUser.setFirstName("TestFirstName");
        testUser.setLastName("TestLastName");
        testUser.setEmail("test@test.com");
        testUser.setPassword("qwerty");
        testUser = userRepository.save(testUser);
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteById(testUser.getId());
    }

    @Test
    public void testIndex()
            throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Assertions.assertTrue(response.contains(testUser.getEmail()));
    }
}

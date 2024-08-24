package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTest {
    private final String URL = "/api/users";
    private final String ADMIN = "hexlet@example.com";
    private final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token =
            SecurityMockMvcRequestPostProcessors.jwt().jwt(builder -> builder.subject(ADMIN));
    private final UserCreateDTO createTestUserDTO = new UserCreateDTO("Ivan", "Ivanov", "ivanov@example.com", "qwerty");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URL).with(token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Assertions.assertTrue(response.contains(ADMIN));
    }

    @Test
    public void testShow() throws Exception {
        UserDTO user = userService.create(createTestUserDTO);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(URL + "/" + user.getId())
                        .with(token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        UserDTO responseUser = objectMapper.readValue(response,
                new TypeReference<UserDTO>() {});
        Assertions.assertEquals(user, responseUser);
        userService.deleteById(user.getId());
    }

    @Test
    public void testCreate() throws Exception {
        String jsonUser = objectMapper.writeValueAsString(createTestUserDTO);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(URL)
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        UserDTO responseUser = objectMapper.readValue(response,
                new TypeReference<UserDTO>() {});
        UserDTO user = userService.findById(responseUser.getId());
        Assertions.assertEquals(responseUser, user);
        userService.deleteById(user.getId());
    }

    @Test
    public void testUpdate() throws Exception {
        UserDTO user = userService.create(createTestUserDTO);
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        String updateEmail = "update@example.com";
        updateDTO.setEmail(JsonNullable.of(updateEmail));
        String jsonUpdate = objectMapper.writeValueAsString(updateDTO);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(URL + "/" + user.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdate))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        UserDTO updated = userService.findById(user.getId());
        Assertions.assertEquals(updateEmail, updated.getEmail());
        userService.deleteById(user.getId());
    }

    @Test
    public void testDelete() throws Exception {
        UserDTO user = userService.create(createTestUserDTO);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.delete(URL + "/" + user.getId())
                        .with(token))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();
        Assertions.assertThrowsExactly(ResourceNotFoundException.class, () -> userService.findById(user.getId()));
    }

}

package hexlet.code.controller;

import hexlet.code.service.TaskStatusService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskStatusControllerTest {
    private final String URL = "/api/task_statuses";
    private final String ADMIN = "hexlet@example.com";
    private final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token =
            SecurityMockMvcRequestPostProcessors.jwt().jwt(builder -> builder.subject(ADMIN));

    @Autowired
    private TaskStatusService taskStatusService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URL).with(token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Assertions.assertTrue(response.contains("Draft"));
    }

}

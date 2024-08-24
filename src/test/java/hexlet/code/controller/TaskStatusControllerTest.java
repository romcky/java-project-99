package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.dto.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.service.TaskStatusService;
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
public class TaskStatusControllerTest {
    private final String URL = "/api/task_statuses";
    private final String ADMIN = "hexlet@example.com";
    private final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token =
            SecurityMockMvcRequestPostProcessors.jwt().jwt(builder -> builder.subject(ADMIN));
    private final TaskStatusCreateDTO createDTO = new TaskStatusCreateDTO("Example", "example");

    @Autowired
    private TaskStatusService taskStatusService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URL).with(token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Assertions.assertTrue(response.contains("Draft"));
    }

    @Test
    public void testShow() throws Exception {
        TaskStatusDTO status = taskStatusService.create(createDTO);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(URL + "/" + status.getId())
                        .with(token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        TaskStatusDTO responseDTO = objectMapper.readValue(response,
                new TypeReference<>() {});
        Assertions.assertEquals(status, responseDTO);
        taskStatusService.deleteById(status.getId());
    }

    @Test
    public void testCreate() throws Exception {
        String jsonStatus = objectMapper.writeValueAsString(createDTO);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(URL)
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStatus))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        TaskStatusDTO responseDTO = objectMapper.readValue(response,
                new TypeReference<>() {});
        TaskStatusDTO status = taskStatusService.findById(responseDTO.getId());
        Assertions.assertEquals(responseDTO, status);
        taskStatusService.deleteById(status.getId());
    }

    @Test
    public void testUpdate() throws Exception {
        TaskStatusDTO status = taskStatusService.create(createDTO);
        TaskStatusUpdateDTO updateDTO = new TaskStatusUpdateDTO();
        String updateSlug = "update";
        updateDTO.setSlug(JsonNullable.of(updateSlug));
        String jsonUpdate = objectMapper.writeValueAsString(updateDTO);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(URL + "/" + status.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdate))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        TaskStatusDTO updated = taskStatusService.findById(status.getId());
        Assertions.assertEquals(updateSlug, updated.getSlug());
        taskStatusService.deleteById(status.getId());
    }

    @Test
    public void testDelete() throws Exception {
        TaskStatusDTO status = taskStatusService.create(createDTO);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.delete(URL + "/" + status.getId())
                        .with(token))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();
        Assertions.assertThrowsExactly(ResourceNotFoundException.class, () -> taskStatusService.findById(status.getId()));
    }

}

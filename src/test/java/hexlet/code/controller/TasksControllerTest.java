package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
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

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class TasksControllerTest {
    private final String URL = "/api/tasks";
    private final String ADMIN = "hexlet@example.com";
    private final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token =
            SecurityMockMvcRequestPostProcessors.jwt().jwt(builder -> builder.subject(ADMIN));
    private final TaskCreateDTO createDTO = new TaskCreateDTO("title", 0, "content", "draft", 1L, List.of(1L, 2L));

    @Autowired
    private TaskService taskService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TaskMapper taskMapper;
    @Autowired
    TaskRepository taskRepository;

    @Test
    public void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URL).with(token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Assertions.assertTrue(response.contains("[]"));
    }

    @Test
    public void testShow() throws Exception {
        TaskDTO task = taskService.create(createDTO);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(URL + "/" + task.getId())
                        .with(token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        TaskDTO responseDTO = objectMapper.readValue(response,
                new TypeReference<>() {});
        Assertions.assertEquals(task, responseDTO);
        taskService.deleteById(task.getId());
    }


    @Test
    public void testCreate() throws Exception {
        String jsonTask = objectMapper.writeValueAsString(createDTO);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(URL)
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTask))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        TaskDTO taskDTO = objectMapper.readValue(response,
                new TypeReference<TaskDTO>() {
                });
        Task task = taskRepository.findByName(createDTO.getTitle()).orElseThrow();
        Assertions.assertEquals(task.getDescription(), taskDTO.getContent());
        taskRepository.deleteById(task.getId());
    }

    @Test
    public void testUpdate() throws Exception {
        TaskDTO task = taskService.create(createDTO);
        TaskUpdateDTO updateDTO = new TaskUpdateDTO();
        String updateTitle = "update";
        updateDTO.setTitle(JsonNullable.of(updateTitle));
        String jsonUpdate = objectMapper.writeValueAsString(updateDTO);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(URL + "/" + task.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdate))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Task updated = taskRepository.findByName(updateTitle).orElseThrow();
        Assertions.assertEquals(updated.getDescription(), createDTO.getContent());
        taskService.deleteById(updated.getId());
    }

    @Test
    public void testDelete() throws Exception {
        TaskDTO task = taskService.create(createDTO);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.delete(URL + "/" + task.getId())
                        .with(token))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();
        Assertions.assertThrowsExactly(ResourceNotFoundException.class, () -> taskService.findById(task.getId()));
    }

}

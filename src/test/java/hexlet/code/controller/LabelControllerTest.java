package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.service.LabelService;
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
public class LabelControllerTest {
    private final String URL = "/api/labels";
    private final String ADMIN = "hexlet@example.com";
    private final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token =
            SecurityMockMvcRequestPostProcessors.jwt().jwt(builder -> builder.subject(ADMIN));
    private final LabelCreateDTO createDTO = new LabelCreateDTO("example");

    @Autowired
    private LabelService labelService;
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
        Assertions.assertTrue(response.contains("bug"));
    }

    @Test
    public void testShow() throws Exception {
        LabelDTO label = labelService.create(createDTO);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(URL + "/" + label.getId())
                        .with(token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        LabelDTO responseDTO = objectMapper.readValue(response,
                new TypeReference<>() {});
        Assertions.assertEquals(label, responseDTO);
        labelService.deleteById(label.getId());
    }

    @Test
    public void testCreate() throws Exception {
        String jsonLabel = objectMapper.writeValueAsString(createDTO);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(URL)
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLabel))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        LabelDTO responseDTO = objectMapper.readValue(response,
                new TypeReference<>() {});
        LabelDTO label = labelService.findById(responseDTO.getId());
        Assertions.assertEquals(responseDTO, label);
        labelService.deleteById(label.getId());
    }

    @Test
    public void testUpdate() throws Exception {
        LabelDTO status = labelService.create(createDTO);
        LabelUpdateDTO updateDTO = new LabelUpdateDTO();
        String updateName = "update";
        updateDTO.setName(updateName);
        String jsonUpdate = objectMapper.writeValueAsString(updateDTO);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(URL + "/" + status.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdate))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        LabelDTO updated = labelService.findById(status.getId());
        Assertions.assertEquals(updateName, updated.getName());
        labelService.deleteById(status.getId());
    }

    @Test
    public void testDelete() throws Exception {
        LabelDTO status = labelService.create(createDTO);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.delete(URL + "/" + status.getId())
                        .with(token))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();
        Assertions.assertThrowsExactly(ResourceNotFoundException.class, () -> labelService.findById(status.getId()));
    }

}

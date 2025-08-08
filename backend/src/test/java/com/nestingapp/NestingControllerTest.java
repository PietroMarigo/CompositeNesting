package com.nestingapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class NestingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void nestEndpointReturnsPlacement() throws Exception {
        String svg = "<svg><polygon points='0,0 1,0 1,1 0,1'/></svg>";
        MockMultipartFile file = new MockMultipartFile("files", "square.svg", "image/svg+xml", svg.getBytes());
        String config = new ObjectMapper().writeValueAsString(new NestingConfig(0, 0, 10, 10, 1));
        String response = mockMvc.perform(multipart("/api/nest").file(file).param("config", config))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        JsonNode root = new ObjectMapper().readTree(response);
        assertEquals(1, root.get("nestedParts").size());
    }
}

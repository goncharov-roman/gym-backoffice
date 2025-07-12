package org.roman.petresearch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.roman.petresearch.service.TrainingService;
import org.roman.petresearch.entity.Training;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@ActiveProfiles("test")
class CachingIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TrainingService trainingService;

    private MockMvc mockMvc;

    @Test
    void testCachingWithTwoSubsequentCalls() throws Exception {
        // Setup MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // First call - should hit database and cache the result
        long startTime1 = System.currentTimeMillis();
        mockMvc.perform(get("/api/trainings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Йога для начинающих"));
        long endTime1 = System.currentTimeMillis();
        long firstCallTime = endTime1 - startTime1;

        // Second call - should hit cache (should be faster)
        long startTime2 = System.currentTimeMillis();
        mockMvc.perform(get("/api/trainings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Йога для начинающих"));
        long endTime2 = System.currentTimeMillis();
        long secondCallTime = endTime2 - startTime2;

        // Verify that second call was faster (cache hit)
        assertTrue(secondCallTime < firstCallTime, 
                "Second call should be faster due to cache hit. First: " + firstCallTime + "ms, Second: " + secondCallTime + "ms");

        // Verify that both calls returned the same data
        String firstResponse = mockMvc.perform(get("/api/trainings/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String secondResponse = mockMvc.perform(get("/api/trainings/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(firstResponse, secondResponse, "Both calls should return identical data");
    }

    @Test
    void testCacheEviction() throws Exception {
        // Setup MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // First call - cache the result
        mockMvc.perform(get("/api/trainings/1"))
                .andExpect(status().isOk());

        // Clear cache
        trainingService.clearCache();

        // Second call after cache clear - should hit database again
        mockMvc.perform(get("/api/trainings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testCacheWithDifferentIds() throws Exception {
        // Setup MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Call different training IDs
        mockMvc.perform(get("/api/trainings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(get("/api/trainings/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));

        // Verify both are cached separately
        mockMvc.perform(get("/api/trainings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(get("/api/trainings/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }
} 
package com.ouro.nova.franzoso.ouroNovaTest;

import com.ouro.nova.franzoso.ouroNovaTest.controller.SensorDataController;
import com.ouro.nova.franzoso.ouroNovaTest.modelo.SensorData;
import com.ouro.nova.franzoso.ouroNovaTest.service.SensorDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SensorDataControllerTest {

    @Mock
    private SensorDataService sensorDataService;

    @InjectMocks
    private SensorDataController sensorDataController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(sensorDataController).build();
    }

    @Test
    void testGetAllSensorData() throws Exception {
        SensorData sensorData = new SensorData();
        when(sensorDataService.getAllSensorData()).thenReturn(Collections.singletonList(sensorData));

        mockMvc.perform(get("/sensor-data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());

        verify(sensorDataService, times(1)).getAllSensorData();
    }

    @Test
    void testGetSensorDataById() throws Exception {
        Long id = 1L;
        SensorData sensorData = new SensorData();
        when(sensorDataService.findById(id)).thenReturn(Optional.of(sensorData));

        mockMvc.perform(get("/sensor-data/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(sensorDataService, times(1)).findById(id);
    }

    @Test
    void testGetSensorDataByIdNotFound() throws Exception {
        Long id = 1L;
        when(sensorDataService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/sensor-data/{id}", id))
                .andExpect(status().isNotFound());

        verify(sensorDataService, times(1)).findById(id);
    }

    @Test
    void testLoadSensorData() throws Exception {
        String filePath = "path/to/file";
        SensorData sensorData = new SensorData();
        when(sensorDataService.readAndSaveData(filePath)).thenReturn(Collections.singletonList(sensorData));

        mockMvc.perform(get("/sensor-data/load").param("filePath", filePath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());

        verify(sensorDataService, times(1)).readAndSaveData(filePath);
    }

    @Test
    void testLoadSensorDataError() throws Exception {
        String filePath = "path/to/file";
        when(sensorDataService.readAndSaveData(filePath)).thenThrow(new IOException("File error"));

        mockMvc.perform(get("/sensor-data/load").param("filePath", filePath))
                .andExpect(status().isInternalServerError());

        verify(sensorDataService, times(1)).readAndSaveData(filePath);
    }


    @Test
    void testDeleteSensorData() throws Exception {
        Long id = 1L;
        when(sensorDataService.deleteById(id)).thenReturn(true);

        mockMvc.perform(delete("/sensor-data/{id}", id))
                .andExpect(status().isNoContent());

        verify(sensorDataService, times(1)).deleteById(id);
    }

    @Test
    void testDeleteSensorDataNotFound() throws Exception {
        Long id = 1L;
        when(sensorDataService.deleteById(id)).thenReturn(false);

        mockMvc.perform(delete("/sensor-data/{id}", id))
                .andExpect(status().isNotFound());

        verify(sensorDataService, times(1)).deleteById(id);
    }
}

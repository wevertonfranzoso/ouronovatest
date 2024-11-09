package com.ouro.nova.franzoso.ouroNovaTest;

import com.ouro.nova.franzoso.ouroNovaTest.modelo.SensorData;
import com.ouro.nova.franzoso.ouroNovaTest.repository.SensorDataRepository;
import com.ouro.nova.franzoso.ouroNovaTest.service.SensorDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SensorDataServiceTest {

    @Mock
    private SensorDataRepository sensorDataRepository;

    @InjectMocks
    private SensorDataService sensorDataService;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetAllSensorData() {
        SensorData data1 = new SensorData(new Date(), Arrays.asList(100.0, 200.0));
        SensorData data2 = new SensorData(new Date(), Arrays.asList(150.0, 250.0));
        when(sensorDataRepository.findAll()).thenReturn(Arrays.asList(data1, data2));

        List<SensorData> result = sensorDataService.getAllSensorData();

        assertEquals(2, result.size(), "Deve retornar 2 dados");
        assertTrue(result.contains(data1), "Deve conter o primeiro dado");
        assertTrue(result.contains(data2), "Deve conter o segundo dado");
    }

    @Test
    void testFindById() {
        SensorData data = new SensorData(new Date(), Arrays.asList(100.0, 200.0));
        when(sensorDataRepository.findById(1L)).thenReturn(Optional.of(data));

        Optional<SensorData> result = sensorDataService.findById(1L);

        assertTrue(result.isPresent(), "Deve retornar o dado");
        assertEquals(data, result.get(), "Deve ser o dado esperado");
    }

    @Test
    void testUpdateSensorData() {
        SensorData originalData = new SensorData(new Date(), Arrays.asList(100.0, 200.0));
        SensorData updatedData = new SensorData(new Date(), Arrays.asList(150.0, 250.0));

        when(sensorDataRepository.findById(1L)).thenReturn(Optional.of(originalData));
        when(sensorDataRepository.save(any(SensorData.class))).thenReturn(updatedData);

        SensorData result = sensorDataService.updateSensorData(1L, updatedData);

        assertEquals(updatedData.getDoubleList(), result.getDoubleList(), "Os valores devem ser atualizados");
        assertEquals(updatedData.getDate(), result.getDate(), "A data deve ser atualizada");
    }

    @Test
    void testDeleteById() {
        when(sensorDataRepository.existsById(1L)).thenReturn(true);

        boolean result = sensorDataService.deleteById(1L);

        assertTrue(result, "Deve retornar verdadeiro ao excluir um dado existente");
        verify(sensorDataRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteByIdNotFound() {
        when(sensorDataRepository.existsById(1L)).thenReturn(false);

        boolean result = sensorDataService.deleteById(1L);

        assertFalse(result, "Deve retornar falso ao tentar excluir um dado não encontrado");
        verify(sensorDataRepository, times(0)).deleteById(1L);
    }
}

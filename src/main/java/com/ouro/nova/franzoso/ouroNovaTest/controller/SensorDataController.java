package com.ouro.nova.franzoso.ouroNovaTest.controller;

import com.ouro.nova.franzoso.ouroNovaTest.modelo.SensorData;
import com.ouro.nova.franzoso.ouroNovaTest.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sensor-data")
public class SensorDataController {

    @Autowired
    private SensorDataService sensorDataService;

    @PostMapping
    public ResponseEntity<SensorData> saveSensorData(@RequestBody SensorData sensorData) {
        SensorData savedData = sensorDataService.saveSensorData(sensorData);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedData);
    }

    // Endpoint para carregar dados do arquivo
    @GetMapping("/load")
    public ResponseEntity<List<SensorData>> loadSensorData(@RequestParam String filePath) {
        try {
            List<SensorData> dataList = sensorDataService.readAndSaveData(filePath);
            return ResponseEntity.ok(dataList);
        } catch (IOException | ParseException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // Endpoint para buscar todos os dados de sensores
    @GetMapping
    public ResponseEntity<List<SensorData>> getAllSensorData() {
        return ResponseEntity.ok(sensorDataService.getAllSensorData());
    }

    // Endpoint para buscar dados de sensor por ID
    @GetMapping("/{id}")
    public ResponseEntity<SensorData> getSensorDataById(@PathVariable Long id) {
        Optional<SensorData> data = sensorDataService.findById(id);
        return data.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // Endpoint para atualizar os dados de um sensor
    @PutMapping("/{id}")
    public ResponseEntity<SensorData> updateSensorData(
            @PathVariable Long id, @RequestBody SensorData updatedData) {
        Optional<SensorData> data = sensorDataService.findById(id);
        if (data.isPresent()) {
            SensorData savedData = sensorDataService.updateSensorData(id, updatedData);
            return ResponseEntity.ok(savedData);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Endpoint para excluir dados de sensor por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensorData(@PathVariable Long id) {
        if (sensorDataService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

package com.ouro.nova.franzoso.ouroNovaTest.service;

import com.ouro.nova.franzoso.ouroNovaTest.modelo.SensorData;
import com.ouro.nova.franzoso.ouroNovaTest.repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SensorDataService {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");

    @Autowired
    private SensorDataRepository sensorDataRepository;

    public SensorData saveSensorData(SensorData sensorData) {
        return sensorDataRepository.save(sensorData);
    }

    public List<SensorData> readAndSaveData(String filePath) throws IOException, ParseException {
        List<SensorData> sensorDataList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                SensorData data = parseLine(line);
                sensorDataRepository.save(data);
                sensorDataList.add(data);
            }
        }
        return sensorDataList;
    }

    public List<SensorData> getAllSensorData() {
        return sensorDataRepository.findAll();
    }

    public Optional<SensorData> findById(Long id) {
        return sensorDataRepository.findById(id);
    }

    public SensorData updateSensorData(Long id, SensorData updatedData) {
        return sensorDataRepository.findById(id)
                .map(existingData -> {
                    existingData.setDate(updatedData.getDate());
                    existingData.setDoubleList(updatedData.getDoubleList());
                    return sensorDataRepository.save(existingData);
                })
                .orElseThrow(() -> new RuntimeException("SensorData not found with id: " + id));
    }

    public boolean deleteById(Long id) {
        if (sensorDataRepository.existsById(id)) {
            sensorDataRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private SensorData parseLine(String line) throws ParseException {
        String[] parts = line.split(";");
        Date date = DATE_FORMAT.parse(parts[0]);

        List<Double> values = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            values.add(Double.parseDouble(parts[i]));
        }

        return new SensorData(date, values);
    }
}
package com.ouro.nova.franzoso.ouroNovaTest.modelo;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;

    @ElementCollection
    private List<Double> doubleList;

    // Construtores
    public SensorData() {}

    public SensorData(Date date, List<Double> doubleList) {
        this.date = date;
        this.doubleList = doubleList;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Double> getDoubleList() {
        return doubleList;
    }

    public void setDoubleList(List<Double> doubleList) {
        this.doubleList = doubleList;
    }
}

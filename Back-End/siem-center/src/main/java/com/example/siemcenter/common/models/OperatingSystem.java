package com.example.siemcenter.common.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatingSystem implements Comparable<OperatingSystem> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    public OperatingSystem(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(OperatingSystem os) {
        return os.getName().compareTo(this.getName());
    }
}

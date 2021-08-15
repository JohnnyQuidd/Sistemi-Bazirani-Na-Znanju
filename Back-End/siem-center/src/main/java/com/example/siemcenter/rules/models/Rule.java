package com.example.siemcenter.rules.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String content;

    @JsonIgnore
    @OneToMany
    private List<Fact> factList;

    @JsonIgnore
    @OneToMany
    private List<Action> actionList;
}

package com.example.siemcenter.rules.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(name="content", columnDefinition="LONGTEXT")
    private String content;

    @JsonIgnore
    @OneToMany
    private List<Fact> factList;

    @JsonIgnore
    @OneToMany
    private List<Action> actionList;

    public String getContent() {
        return content;
    }
}

package com.example.recruitment_task.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Article {
    private int id;

    private String url;
    private String title;
    private LocalDateTime date;

    @JsonIgnore
    private String text;

    private String city;
    private String state;
}
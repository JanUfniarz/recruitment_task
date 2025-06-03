package com.example.recruitment_task.entities;

public record City(
        String name,
        String state
) {
    public String getFullName() {
        return name + "_" + state;
    }
}

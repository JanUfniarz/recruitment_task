package com.example.recruitment_task.entities;

import java.time.LocalDateTime;

public record Article(
        String url,
        String title,
        LocalDateTime date,
        String text,
        String city,
        String state
) {}

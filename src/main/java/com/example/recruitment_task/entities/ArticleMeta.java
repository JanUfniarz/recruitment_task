package com.example.recruitment_task.entities;

import java.time.LocalDateTime;

public record ArticleMeta(
        int id,
        String url,
        String title,
        LocalDateTime date,
        String city,
        String state
) {}

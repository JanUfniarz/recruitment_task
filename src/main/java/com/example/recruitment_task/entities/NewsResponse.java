package com.example.recruitment_task.entities;

import java.util.List;

public record NewsResponse(
        List<Article> local,
        List<Article> state,
        List<Article> global
) {}

package com.example.recruitment_task.entities;

import java.util.List;

public record NewsResponse(
        List<ArticleMeta> local,
        List<ArticleMeta> state,
        List<ArticleMeta> global
) {}

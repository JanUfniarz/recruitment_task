package com.example.recruitment_task.controllers;

import com.example.recruitment_task.entities.Article;
import com.example.recruitment_task.entities.City;
import com.example.recruitment_task.entities.NewsResponse;
import com.example.recruitment_task.services.CitiesService;
import com.example.recruitment_task.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping("api/news")
public class NewsController {
    private final CitiesService citiesService;
    private final NewsService newsService;

    @Autowired
    public NewsController(
            CitiesService citiesService,
            NewsService newsService
    ) {
        this.citiesService = citiesService;
        this.newsService = newsService;
    }

    @GetMapping
    public NewsResponse getNews(
            @RequestParam("city") String fullName
    ) {
        City city = citiesService.getByFullName(fullName);
        return newsService.getLocalNews(city);
    }

    @GetMapping(path = "{id}")
    public Article getFullArticle(
            @PathVariable("id") int id
    ) {
        return newsService.getById(id);
    }
}

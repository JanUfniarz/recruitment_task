package com.example.recruitment_task.controllers;

import com.example.recruitment_task.entities.Article;
import com.example.recruitment_task.entities.City;
import com.example.recruitment_task.services.CitiesService;
import com.example.recruitment_task.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public List<Article> getNews(
            @RequestParam("city") String fullName
    ) {
        City city = citiesService.getByFullName(fullName);
        return newsService.getLocalNews(city);
    }
}

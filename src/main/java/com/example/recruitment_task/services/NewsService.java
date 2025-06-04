package com.example.recruitment_task.services;

import com.example.recruitment_task.daos.NewsDAO;
import com.example.recruitment_task.entities.Article;
import com.example.recruitment_task.entities.City;
import com.example.recruitment_task.entities.NewsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NewsService {
    private final NewsDAO dao;

    @Autowired
    public NewsService(NewsDAO dao) {
        this.dao = dao;
    }

    public NewsResponse getLocalNews(City city) {
        List<Article> global = dao.getArticles().stream()
                .filter(article -> article.city().equals("GLOBAL"))
                .toList();

        Map<Boolean, List<Article>> partitioned = dao.getArticles().stream()
                .filter(article -> article.state().equals(city.state()))
                .collect(Collectors.partitioningBy(
                        article -> article.city().equals(city.name())
                ));

        return new NewsResponse(
                partitioned.get(true),
                partitioned.get(false),
                global
        );
    }
}

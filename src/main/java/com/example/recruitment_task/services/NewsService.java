package com.example.recruitment_task.services;

import com.example.recruitment_task.daos.NewsDAO;
import com.example.recruitment_task.entities.Article;
import com.example.recruitment_task.entities.ArticleMeta;
import com.example.recruitment_task.entities.City;
import com.example.recruitment_task.entities.NewsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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

    private List<ArticleMeta> sorted(List<ArticleMeta> articles) {
        return articles.stream().sorted(Comparator.comparing(ArticleMeta::date)).toList();
    }

    public NewsResponse getLocalNews(City city) {
        List<ArticleMeta> global = dao.getArticles().stream()
                .filter(article -> article.city().equals("GLOBAL"))
                .map(Article::toMeta)
                .toList();

        Map<Boolean, List<ArticleMeta>> partitioned = dao.getArticles().stream()
                .filter(article -> article.state().equals(city.state()))
                .map(Article::toMeta)
                .collect(Collectors.partitioningBy(
                        article -> article.city().equals(city.name())
                ));

        return new NewsResponse(
                sorted(partitioned.get(true)),
                sorted(partitioned.get(false)),
                sorted(global)
        );
    }

    public Article getById(int id) {
        return dao.getArticles().stream()
                .filter(article -> article.id() == id)
                .findFirst()
                .orElseThrow();
    }
}

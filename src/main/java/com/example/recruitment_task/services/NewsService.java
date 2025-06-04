package com.example.recruitment_task.services;

import com.example.recruitment_task.daos.NewsDAO;
import com.example.recruitment_task.entities.Article;
import com.example.recruitment_task.entities.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    private NewsDAO dao;

    @Autowired
    public NewsService(NewsDAO dao) {
        this.dao = dao;
    }

    public List<Article> getLocalNews(City city) {

    }
}

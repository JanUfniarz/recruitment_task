package com.example.recruitment_task.daos;

import com.example.recruitment_task.entities.Article;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class NewsDAO {
    private final String DATA_PATH = "data/transformed_articles.json";

    private List<Article> articles;

    private final ObjectMapper mapper;

    public NewsDAO() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    public List<Article> getArticles() {
        if (articles == null) {
            try {
                ClassPathResource resource = new ClassPathResource(DATA_PATH);
                try (InputStream inputStream = resource.getInputStream()) {
                    CollectionType listType = mapper.getTypeFactory()
                            .constructCollectionType(List.class, Article.class);
                    articles = mapper.readValue(inputStream, listType);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to load articles from " + DATA_PATH, e);
            }
        }
        return articles;
    }
}

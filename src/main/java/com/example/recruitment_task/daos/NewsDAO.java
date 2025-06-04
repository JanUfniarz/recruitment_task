package com.example.recruitment_task.daos;

import com.example.recruitment_task.entities.Article;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class NewsDAO {
    @SuppressWarnings("FieldCanBeLocal")
    private final String DATA_PATH = "data/transformed_articles.json";

    private List<Article> articles;

    public List<Article> getArticles() {
        if (articles == null) {
            ObjectMapper mapper = new ObjectMapper();

            mapper.registerModule(new JavaTimeModule());

            CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Article.class);

            try {
                articles = mapper.readValue(new File(DATA_PATH), listType);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return articles;
    }
}

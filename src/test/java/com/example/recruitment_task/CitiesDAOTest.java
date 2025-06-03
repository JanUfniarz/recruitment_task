package com.example.recruitment_task;

import com.example.recruitment_task.entities.City;
import com.example.recruitment_task.services.CitiesDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CitiesDAOTest {
    @Autowired
    private CitiesDAO dao;

    @Test
    void shouldLoadCitiesFromJson() {
        List<City> cities = dao.getCities();
        assertNotNull(cities);
        assertFalse(cities.isEmpty());

        City first = cities.get(0);
        System.out.println(first);

        assertNotNull(first.name());
        assertNotNull(first.state());
    }
}

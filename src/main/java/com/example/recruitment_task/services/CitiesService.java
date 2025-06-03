package com.example.recruitment_task.services;

import com.example.recruitment_task.entities.City;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor
public class CitiesService {
    @SuppressWarnings("FieldCanBeLocal")
    private final String DATA_PATH = "data/us-cities-demographics.json";

    private List<City> cities;
    public List<City> getCities() {
        if (cities == null) try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(DATA_PATH)) {
            if (inputStream == null)
                throw new RuntimeException("Nie znaleziono pliku cities.json");

            JsonNode root = (new ObjectMapper()).readTree(inputStream);
            List<City> res = new ArrayList<>();

            for (JsonNode node : root) {
                String name = node.get("city").asText();
                String state = node.get("state_code").asText();
                res.add(new City(name, state));
            }

            this.cities = res;
        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas ładowania miast", e);
        }
        return cities;
    }
}

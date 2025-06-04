package com.example.recruitment_task.daos;

import com.example.recruitment_task.entities.City;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class CitiesDAO {
    @SuppressWarnings("FieldCanBeLocal")
    private final String DATA_PATH = "data/us_states_and_cities.json";

    private List<City> cities;

    public List<City> getCities() {
        if (cities == null) {
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(DATA_PATH)) {
                if (inputStream == null)
                    throw new RuntimeException("Nie znaleziono pliku us-cities-demographics.json");

                JsonNode root = new ObjectMapper().readTree(inputStream);
                List<City> res = new ArrayList<>();

                // iterujemy po polach mapy (klucz = stan, wartość = tablica miast)
                Iterator<Map.Entry<String, JsonNode>> fields = root.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    String state = entry.getKey();
                    JsonNode citiesArray = entry.getValue();

                    for (JsonNode cityNode : citiesArray) {
                        String name = cityNode.asText();

                        res.add(new City(name, state));
                    }
                }

                this.cities = res;
            } catch (Exception e) {
                throw new RuntimeException("Błąd podczas ładowania miast", e);
            }
        }

        return cities;
    }
}

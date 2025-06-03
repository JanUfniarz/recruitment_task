package com.example.recruitment_task.services;

import com.example.recruitment_task.entities.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitiesService {
    private CitiesDAO dao;

    @Autowired
    public CitiesService(CitiesDAO citiesDAO) {
        this.dao = citiesDAO;
    }

    public List<City> findByQuery(String query) {
        String lowerQuery = query.toLowerCase();

        return dao.getCities().stream()
                .filter(city -> city.name().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }
}

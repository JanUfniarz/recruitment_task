package com.example.recruitment_task.services;

import com.example.recruitment_task.daos.CitiesDAO;
import com.example.recruitment_task.entities.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitiesService {
    private final CitiesDAO dao;

    @Autowired
    public CitiesService(CitiesDAO citiesDAO) {
        this.dao = citiesDAO;
    }

    public List<City> findByQuery(String query) {
        return dao.getCities().stream()
                .filter(city -> city
                        .name()
                        .toLowerCase()
                        .contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public City getByFullName(String fullName) {
        return dao.getCities().stream()
                .filter(city -> city.getFullName().equals(fullName))
                .findFirst()
                .orElseThrow();
    }
}

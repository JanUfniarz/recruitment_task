package com.example.recruitment_task.services;

import com.example.recruitment_task.api_clients.CityClient;
import com.example.recruitment_task.entities.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CitiesService {
    private final CityClient client;
    private final List<City> cities;

    @Autowired
    public CitiesService(CityClient cityClient) {
        this.client = cityClient;
        cities = new ArrayList<>();
    }
}

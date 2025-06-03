package com.example.recruitment_task.controllers;

import com.example.recruitment_task.entities.City;
import com.example.recruitment_task.services.CitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/cities")
public class CitiesController {

    private final CitiesService service;

    @Autowired
    public CitiesController(CitiesService citiesService) {
        this.service = citiesService;
    }

    @SuppressWarnings("unused")
    @GetMapping
    public List<City> searchCities(
            @RequestParam("query") String query
    ) {
        return service.findByQuery(query);
    }

}

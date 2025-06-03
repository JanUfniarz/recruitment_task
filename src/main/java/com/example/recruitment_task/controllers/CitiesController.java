package com.example.recruitment_task.controllers;

import com.example.recruitment_task.services.CitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cities")
public class CitiesController {

    private final CitiesService service;

    @Autowired
    public CitiesController(CitiesService citiesService) {
        this.service = citiesService;
    }



}

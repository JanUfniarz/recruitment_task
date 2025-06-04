package com.example.recruitment_task.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@SuppressWarnings("unused")
@Controller
class AppController {
    @RequestMapping("/")
    public String app() {
        return "index.html";
    }

    @GetMapping(value = {"/{path:[^\\.]*}", "/**/{path:[^\\.]*}"})
    public String redirect() {
        return "forward:/index.html";
    }
}

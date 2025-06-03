package com.example.recruitment_task.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SuppressWarnings("unused")
@Controller
class AppController {
    @RequestMapping("/") String app() {
        return "index.html";
    }
}

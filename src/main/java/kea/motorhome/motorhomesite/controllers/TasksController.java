package kea.motorhome.motorhomesite.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TasksController {

    @GetMapping("/tasks")
    public String tasks(){
        return "tasks";
    }


}


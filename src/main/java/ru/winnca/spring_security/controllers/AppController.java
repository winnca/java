package ru.winnca.spring_security.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.winnca.spring_security.models.Application;
import ru.winnca.spring_security.models.MyUser;
import ru.winnca.spring_security.services.AppService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1")
public class AppController {
    private AppService service;

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome to the unprotected page";
    }

    @GetMapping("/all-apps")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<Application> allApplications(){
        return service.allApplications();
    }

    @GetMapping("/app/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Application applicationById(@PathVariable int id){
        return service.applicationById(id);
    }

    @PostMapping("/new-user")
    public String addUser(@RequestBody MyUser user){
        service.addUser(user);
        return "user saved";
    }
}
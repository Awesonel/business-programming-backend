package ru.spbu.project.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application")
public class ApplicationController {

  @GetMapping("")
  public ResponseEntity<String> checkApp() {
    return new ResponseEntity<>("Server is running.", HttpStatus.OK);
  }
}

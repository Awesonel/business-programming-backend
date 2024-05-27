package ru.spbu.project.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Application Controller", description="To test the launch of the project.")
@RestController
@RequestMapping("/application")
public class ApplicationController {

  @Operation(
          summary = "Functionality check.",
          description = "Allows you to check the functionality of the controllers."
  )
  @GetMapping("")
  public ResponseEntity<String> checkApp() {
    return new ResponseEntity<>("Server is running.", HttpStatus.OK);
  }
}

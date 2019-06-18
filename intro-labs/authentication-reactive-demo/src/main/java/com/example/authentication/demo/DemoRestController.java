package com.example.authentication.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoRestController {

  @GetMapping("/")
  public String hello() {
    return "it works!";
  }
}

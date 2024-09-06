package com.example.bfandemo.server.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

  @GetMapping("/")
  public String defaultMessage() {
    return "Welcome to my BFAN production application default endpoint!";
  }
}
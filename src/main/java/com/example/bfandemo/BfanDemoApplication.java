package com.example.bfandemo;

import com.example.bfandemo.server.ServerState;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BfanDemoApplication {

  public static void main(String[] args) {

    ServerState state = new ServerState();

    SpringApplication.run(BfanDemoApplication.class, args);
  }

}

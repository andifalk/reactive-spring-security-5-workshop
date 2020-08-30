package com.example.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;
import reactor.tools.agent.ReactorDebugAgent;

@SpringBootApplication
public class ReactiveServerApplication {

  public static void main(String[] args) {
    // Do not activate both, Blockhound and ReactorDebugAgent on Java 8:
    // https://github.com/reactor/BlockHound/issues/70

    BlockHound.install();
    ReactorDebugAgent.init();

    SpringApplication.run(ReactiveServerApplication.class, args);
  }
}

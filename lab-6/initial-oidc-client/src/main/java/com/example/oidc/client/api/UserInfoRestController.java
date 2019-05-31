package com.example.oidc.client.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class UserInfoRestController {

  @GetMapping("/userinfo")
  Mono<Map<String, Object>> userInfo() {
    return Mono.empty();
  }
}

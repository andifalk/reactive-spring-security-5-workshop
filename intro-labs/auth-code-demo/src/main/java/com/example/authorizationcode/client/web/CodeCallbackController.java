package com.example.authorizationcode.client.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.net.URL;

@Controller
public class CodeCallbackController {

  @Value("${democlient.token.endpoint}")
  private URL tokenEndpointUrl;

  @Value("${democlient.token.clientid}")
  private String clientid;

  @Value("${democlient.token.client-secret}")
  private String clientSecret;

  @Value("${democlient.token.redirect-uri}")
  private URI redirectUri;

  @GetMapping(path = "/callback")
  public String oauthCallBack(
      @RequestParam(name = "code", required = false) String code,
      @RequestParam(name = "state", required = false) String state,
      @RequestParam(name = "error", required = false) String error,
      @RequestParam(name = "error_description", required = false) String error_description,
      Model model) {

    if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(state)) {
      model.addAttribute("token_endpoint", tokenEndpointUrl.toString());
      model.addAttribute("grant_type", "authorization_code");
      model.addAttribute("code", code);
      model.addAttribute("state", state);
      model.addAttribute("redirect_uri", redirectUri.toString());
      model.addAttribute("client_id", clientid);
      model.addAttribute("client_secret", clientSecret);
      model.addAttribute("tokenRequest", new TokenRequest());
      return "authcode";
    } else {
      model.addAttribute("error", error);
      model.addAttribute("error_description", error_description);
      return "error";
    }
  }
}

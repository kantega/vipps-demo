package no.kantega.vippsdemo.controller;

import no.kantega.vippsdemo.service.OAuthClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class VippsLoginController {

    private static final String authorizationRequestBaseUri = "oauth2/authorize-client";
    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

    // @Autowired
    // private AuthenticationManager authenticationManager;

    private OAuthClientService oAuthClientService;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    // @Autowired
    // private TokenProvider tokenProvider;

    @Autowired
    public VippsLoginController(OAuthClientService oauthService) {
        oAuthClientService = oauthService;
    }

    @PostMapping("/vipps_login")
    public ResponseEntity<?> getLoginPage(HttpServletRequest request) {

        oAuthClientService.createOauthSession(request);

        return ResponseEntity.ok("ok");
    }
}

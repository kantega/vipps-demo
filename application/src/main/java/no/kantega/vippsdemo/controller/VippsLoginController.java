package no.kantega.vippsdemo.controller;

import no.kantega.vippsdemo.service.OAuthClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@RequestMapping("login")
@RestController
public class VippsLoginController {

    private OAuthClientService oAuthClientService;

    @Autowired
    public VippsLoginController(OAuthClientService oauthService) {
        oAuthClientService = oauthService;
    }

    @GetMapping(value = "/vipps", produces = MediaType.TEXT_HTML_VALUE)
    public void getLoginPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String loginState = UUID.randomUUID().toString();
        request.getSession().setAttribute("loginState", loginState);
        oAuthClientService.redirectToVipps(response, loginState);
    }

    @GetMapping("/callback")
    public void callback(@RequestParam String code,
                         @RequestParam String state,
                         @RequestParam String scope,
                         HttpServletRequest request) {
        if(!request.getSession().getAttribute("loginState").equals(state)) {
            // que pasa?
        } else {
            oAuthClientService.setGrantCode(code);
            oAuthClientService.getOauthToken();
            oAuthClientService.getUserinfo();
        }

    }

}

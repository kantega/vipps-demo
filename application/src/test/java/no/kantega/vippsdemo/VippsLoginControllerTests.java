package no.kantega.vippsdemo;

import no.kantega.vippsdemo.controller.VippsLoginController;
import no.kantega.vippsdemo.service.OAuthClientService;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VippsLoginControllerTests {

    private MockMvc mockMvc;
    private VippsLoginController vippsController;
    private OAuthClientService oauthService;

    public VippsLoginControllerTests() {

        oauthService = mock(OAuthClientService.class);
        vippsController = new VippsLoginController(oauthService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(vippsController)
                .build();
    }

    @Test
    public void loginToVippsShouldCreateSession() throws Exception {
        this.mockMvc.perform(post("/vipps_login"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("ok")));

        verify(oauthService, times(1))
                .createOauthSession(any());
    }
}

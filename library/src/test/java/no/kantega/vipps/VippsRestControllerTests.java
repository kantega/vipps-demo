package no.kantega.vipps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import no.kantega.vipps.controller.VippsRestController;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


public class VippsRestControllerTests {

    private final IPaymentStatusListener paymentStatusListener = mock(IPaymentStatusListener.class);
    private final IConsentStatusListener consentStatusListener = mock(IConsentStatusListener.class);
    private VippsRestController restController;

    private MockMvc mockMvc;

    public VippsRestControllerTests() {

        restController = new VippsRestController(paymentStatusListener, consentStatusListener);

        mockMvc = MockMvcBuilders
                .standaloneSetup(restController)
                .build();
    }

    /**
     * Test processing callback request to update payment status
     */
    @Test
    public void shouldProcessCallback() throws Exception {

        // Add the 'Authorization' header and 'token'
        this.mockMvc.perform(post("/vipps/v2/payments/order123abc")
                .header("Authorization", "some auth token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andDo(print()).andExpect(status().isOk());

        // Expect a call the registered payment listener, with both parameters set
        verify(paymentStatusListener, times(1)).setPaymentStatus(anyString(), any(), anyString());
    }

    /**
     * Test processing callback request to update payment status with no access token.
     */
    @Test
    public void shouldProcessCallbackNoAuthToken() throws Exception {
        // Create a header which will be checked for authenticity
        // The header will return null for the 'Authorization' query.
        HttpHeaders httpHeaders = mock(HttpHeaders.class);

        this.mockMvc.perform(post("/vipps/v2/payments/order123abc")
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andDo(print()).andExpect(status().isOk());

        // Expect a call the registered payment listener, with authToken set to null
        verify(paymentStatusListener, times(1)).setPaymentStatus(anyString(), any(), isNull());
    }

    @Test
    public void shouldProcessConsents() throws Exception {
        this.mockMvc.perform(delete("/vipps/v2/consents/user123abc"))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void shouldReturnShippingDetails() throws Exception {
        this.mockMvc.perform(post("/vipps/v2/payments/123abc/shippingDetails")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}

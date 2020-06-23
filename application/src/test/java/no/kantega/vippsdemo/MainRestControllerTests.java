package no.kantega.vippsdemo;

import no.kantega.vippsdemo.controller.MainRestController;
import no.kantega.vippsdemo.service.IProductService;
import no.kantega.vippsdemo.service.OrderService;
import no.kantega.vippsdemo.service.PaymentService;
import no.kantega.vippsdemo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MainRestControllerTests {

    private PaymentService paymentService;
    private IProductService productService;
    private OrderService orderService;
    private MainRestController restController;

    private MockMvc mockMvc;

    public MainRestControllerTests() {
        paymentService = mock(PaymentService.class);
        productService = mock(ProductService.class);
        orderService = mock(OrderService.class);

        restController = new MainRestController(productService, orderService, paymentService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(restController)
                .build();
    }

    @Test
    public void shouldReturnOrders() throws Exception {
        this.mockMvc.perform(get("/order")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));

        this.mockMvc.perform(get("/order?user_id=SomeUserId")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    public void shouldProcessPayment() throws Exception {
        // Create a header which will be checked for authenticity
        HttpHeaders httpHeaders = mock(HttpHeaders.class);

        when(paymentService.initCreatePayment(any())).thenReturn("{}");

        this.mockMvc.perform(post("/initiatePayment")
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "   \"userId\": \"123abc\",\n" +
                        "   \"products\": [\n" +
                        "       {\n" +
                        "           \"name\": \"Ferrari\",\n" +
                        "           \"quantity\": 1\n" +
                        "       }\n"+
                        "   ]\n" +
                        "}"))
                .andDo(print()).andExpect(status().isOk());

        // Expect a call to the service method for creating payment
        verify(paymentService, times(1))
                .initCreatePayment(any());
    }

    @Test
    public void shouldReturnProducts() throws Exception {
        this.mockMvc.perform(get("/product")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));

        this.mockMvc.perform(get("/product?name=Product")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("")));
    }
}

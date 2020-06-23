package no.kantega.vippsdemo;

import java.util.ArrayList;
import java.util.List;

import no.kantega.vipps.TransactionStatus;
import no.kantega.vipps.dto.*;
import no.kantega.vippsdemo.dto.PaymentRequestDTO;
import no.kantega.vippsdemo.dto.ProductDTO;
import no.kantega.vippsdemo.service.OrderService;
import no.kantega.vippsdemo.service.ProductService;
import no.kantega.vipps.service.VippsService;
import no.kantega.vippsdemo.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.mockito.Mockito.*;

public class PaymentServiceTests {

    VippsService vippsService = mock(VippsService.class);
    ProductService productService = mock(ProductService.class);
    OrderService orderService = mock(OrderService.class);

    private PaymentService paymentService = new PaymentService(vippsService, orderService, productService);

    @Test
    public void createPayment() throws Exception {

        // Mock the content of this request
        ProductDTO requestProduct = mock(ProductDTO.class);
        List<ProductDTO> requestProductList = new ArrayList<>();
        requestProductList.add(requestProduct);

        // Mock the response when querying the product from our repository
        Product product = mock(Product.class);
        List<Product> products = new ArrayList<>();
        products.add(product);

        // Mock the request coming from the client
        PaymentRequestDTO paymentRequest = mock(PaymentRequestDTO.class);

        // Instrument our calls for products and orders
        when(paymentRequest.getProducts()).thenReturn(requestProductList);

        when(productService.getProductByName(anyString())).thenReturn(product);
        when(requestProduct.getName()).thenReturn("some name");
        when(requestProduct.getQuantity()).thenReturn(1);

        when(product.getPrice()).thenReturn(200F);

        when(paymentRequest.getMobileNumber()).thenReturn("+4712345678");
        when(orderService.createOrder(any())).thenReturn("some order id");

        when(vippsService.createPayment(anyString(), anyString(), anyDouble(), any())).thenReturn("success");

        Assertions.assertEquals("success", paymentService.initCreatePayment(paymentRequest));

        // Check that we called our service module
        verify(vippsService, times(1)).createPayment(anyString(), anyString(), anyDouble(), any());
    }

    @Test
    public void updatePaymentInfo() {

        // Fake some order information
        String order_id = "order123abc";
        String order_status = "some status";
        String authToken = "some auth token";

        // Given
        CallbackTransactionInfoDTO transactionInfo = mock(CallbackTransactionInfoDTO.class);
        when(transactionInfo.getStatus()).thenReturn(TransactionStatus.RESERVED);

        Order order = mock(Order.class);
        when(order.getAuthToken())
                .thenReturn(authToken)
                .thenReturn("")
                .thenReturn(authToken);

        when(orderService.getOrderById(order_id)).thenReturn(java.util.Optional.empty());

        // Then
        // Should fail
        paymentService.updateOrderStatus(order_id, order_status, authToken);

        when(orderService.getOrderById(order_id)).thenReturn(java.util.Optional.of(order));

        // Then
        // Should fail - because we tokens are different
        paymentService.updateOrderStatus(order_id, order_status, "some other auth token");

        // Should succeed - because both tokens are empty strings
        paymentService.updateOrderStatus(order_id, order_status, "");

        // Should succeed - because both tokens are the same
        paymentService.updateOrderStatus(order_id, order_status, authToken);

        // Count and verify the correct number of updates
        verify(orderService, times(2)).setOrderStatus(order_id, order_status);
    }

    @Test
    public void updatePaymentInfoAuthIsNull() {

        // Fake some order information
        String order_id = "order123abc";
        String order_status = "some status";
        String authToken = "some auth token";

        // Given
        CallbackTransactionInfoDTO transactionInfo = mock(CallbackTransactionInfoDTO.class);
        when(transactionInfo.getStatus()).thenReturn(TransactionStatus.RESERVED);

        Order order = mock(Order.class);
        when(order.getAuthToken())
                .thenReturn(authToken)
                .thenReturn(null)
                .thenReturn("")
                .thenReturn(null);

        when(orderService.getOrderById(order_id)).thenReturn(java.util.Optional.of(order));

        // Then
        // Should fail - because token is expected
        paymentService.updateOrderStatus(order_id, order_status, null);

        // Should succeed - because no token is expected
        paymentService.updateOrderStatus(order_id, order_status, authToken);

        // Should succeed - because both tokens are "null"
        paymentService.updateOrderStatus(order_id, order_status, null);

        // Should succeed - because both tokens are null
        paymentService.updateOrderStatus(order_id, order_status, null);

        // Count and verify the correct number of updates
        verify(orderService, times(3)).setOrderStatus(order_id, order_status);
    }
}
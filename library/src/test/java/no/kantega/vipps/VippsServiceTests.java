package no.kantega.vipps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.kantega.vipps.config.IVippsClientConfig;
import no.kantega.vipps.config.VippsClientConfigDev;
import no.kantega.vipps.dto.*;

import no.kantega.vipps.service.VippsService;
import okhttp3.*;
import okio.Buffer;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the Vipps API interface.
 *
 * The tests will provide insurance for sufficiently handling successful and unsuccessful calls to the APIs.
 */
public class VippsServiceTests {

    /**
     * Convenience function for creating a 'generic' fake request.
     */
    @NotNull
    private Request createFakeRequest() {
        return new Request.Builder()
                .header("client_Id", "181289716281372")
                .addHeader("client_Secret", "789087608kjhphlo22312")
                .addHeader("Ocp-Apim-Subscription-Key", "87698679786123")
                .url("http://some.url")
                .post(new FormBody.Builder().build())
                .build();
    }

    /**
     * Convenience function for creating a 'generic' fake token response.
     * @param expires_in Value for when the token expires - in seconds
     * @param expires_on Value for when the token expires - in epoch
     * @param not_before Value for when the token is created, at its earliest - in epoch
     */
    @NotNull
    private Response createFakeTokenResponse(int expires_in, int expires_on, int not_before) {
        // Create a dummy body for the response
        // This is what it will look like when returned from Vipps
        String tokenJSON = "{\n" +
                "    \"token_type\": \"Bearer\",\n" +
                "    \"expires_in\": " + expires_in + ",\n" +
                "    \"ext_expires_in\": 3600,\n" + // Not in use
                "    \"expires_on\": \""+ expires_on + "\",\n" +
                "    \"not_before\": \"" + not_before + "\",\n" +
                "    \"resource\": \"00000002-0000-0000-c000-000000000000\",\n" +
                "    \"access_token\": \"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1Ni\"\n" +
                "}";

        // Put the token body into our fake response object
        ResponseBody tokenBody = ResponseBody.create(tokenJSON, MediaType.get("application/json"));

        return new Response.Builder()
                .addHeader("content-type","application/json; charset=utf-8")
                .code(200)
                .protocol(Protocol.HTTP_2)
                .message("")
                .request(createFakeRequest()) // Needs a valid response object so as not to throw
                .body(tokenBody)
                .build();
    }

    HttpClient httpClient = mock(HttpClient.class);
    IVippsClientConfig clientConfig = new VippsClientConfigDev();

    private final VippsService service = new VippsService(httpClient, clientConfig);

    /**
     * test for correct jsonString when converting InitiatepaymentRequest
     */
    @Test
    public void correctJsonStringInitiatePaymentRequest(){

        MerchantInfoDTO merchantInfoDTO = new MerchantInfoDTO("12345","prefix","http://fallback.com");
        TransactionDTO transactionDTO = new TransactionDTO("88888888", 100F,"a pair of jeans");
        CustomerInfoDTO customerInfoDTO = new CustomerInfoDTO("87654321");

        InitiatePaymentRequestDTO initiatePaymentRequestDTO = new InitiatePaymentRequestDTO(customerInfoDTO,merchantInfoDTO,transactionDTO);

        ObjectMapper mapper = new ObjectMapper();

        try {
            String result = mapper.writeValueAsString(initiatePaymentRequestDTO);
            assertTrue(StringUtils.containsIgnoreCase(result,"12345"));
            assertTrue(StringUtils.containsIgnoreCase(result, "a pair of jeans"));
            assertTrue(StringUtils.containsIgnoreCase(result, "customerInfo"));
            assertTrue(StringUtils.containsIgnoreCase(result, "merchantInfo"));
            assertTrue(StringUtils.containsIgnoreCase(result, "transaction"));


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests if createPayment will get correct response on a bad request
     */
    @Test
    public void createPaymentFail(){

        try {

            // We will fake a request for the payment initiation,
            Request fakeRequest = createFakeRequest();

            // Create a dummy body for the payment response
            String orderJSON = "{}";

            // Put the token body into our fake response objects
            ResponseBody orderBody = ResponseBody.create(orderJSON, MediaType.get("application/json"));

            // Create a fake response with a random (not success) message
            Response fakeOrderResponse = new Response.Builder()
                    .addHeader("content-type","application/json; charset=utf-8")
                    .code(401)
                    .protocol(Protocol.HTTP_2)
                    .message("Unauthorized")
                    .request(fakeRequest)
                    .body(orderBody)
                    .build();

            // Create fake token response
            Response fakeTokenResponse = createFakeTokenResponse(3600, 1547823408, 1547819508);

            // Hijack the return values. first for token, then order
            when(httpClient.sendRequest(any())).thenReturn(fakeTokenResponse).thenReturn(fakeOrderResponse);

            // Make the call
            String createPayment = service.createPayment("+4712345678", "order123abc", 100F,"transactiontest");

            // Check that we have the access token we created for the response
            assertEquals("{}", createPayment);

            // Check that we do make a call to the correct end point
            ArgumentCaptor<Request> argument = ArgumentCaptor.forClass(Request.class);
            verify(httpClient, times(2)).sendRequest(argument.capture());
            assertTrue(argument.getValue().url().toString().contains("/ecomm/v2/payments"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests is createPayment will get correct response(200) on successful request
     */
    @Test
    public void createPaymentSuccess() {

        try {
            // We will fake a request for the payment initiation,
            Request fakeRequest = createFakeRequest();

            // Create a dummy body for the payment response
            String orderJSON = "{\n" +
                    "   \"orderId\": \"order123abc\",\n" +
                    "   \"url\": \"https://api.vipps.no/dwo-api-application/v1/deeplink/vippsgateway?v=2&token=eyJraWQiOiJqd3RrZXkiLC\"\n" +
                    "}";

            // Put the token body into our fake response objects
            ResponseBody orderBody = ResponseBody.create(orderJSON, MediaType.get("application/json"));

            // Create our fake responses
            Response fakeOrderResponse = new Response.Builder()
                    .addHeader("content-type","application/json; charset=utf-8")
                    .code(200)
                    .protocol(Protocol.HTTP_2)
                    .message("")
                    .request(fakeRequest)
                    .body(orderBody)
                    .build();

            Response fakeTokenResponse = createFakeTokenResponse(3600, 1547823408, 1547819508);

            // Hijack the return values. first for token, then order
            when(httpClient.sendRequest(any())).thenReturn(fakeTokenResponse).thenReturn(fakeOrderResponse);

            // Make the call
            String createPayment = service.createPayment("+4712345678", "order123abc", 100F,"transactiontest");

            // See that the response is correct
            assertEquals("{\n" +
                    "   \"orderId\": \"order123abc\",\n" +
                    "   \"url\": \"https://api.vipps.no/dwo-api-application/v1/deeplink/vippsgateway?v=2&token=eyJraWQiOiJqd3RrZXkiLC\"\n" +
                    "}", createPayment);

            // Check that we do make a call to the correct end point
            ArgumentCaptor<Request> argument = ArgumentCaptor.forClass(Request.class);
            verify(httpClient, times(2)).sendRequest(argument.capture());
            assertTrue(argument.getValue().url().toString().contains("/ecomm/v2/payments"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests that we are able to handle failed retrieval
     */
    @Test
    public void getAccessTokenFail() {

        try {
            // We will fake a request for the access token,
            // which in return will yield a response.
            Request fakeRequest = createFakeRequest();

            // Create a dummy body for the response
            // This is what it will look like when returned from Vipps
            String tokenJSON = "{}";

            // Put the token body into our fake response object
            ResponseBody tokenBody = ResponseBody.create(tokenJSON, MediaType.get("application/json"));

            // Create a fake response with a random (not success) message
            Response fakeResponse = new Response.Builder()
                    .addHeader("content-type","application/json; charset=utf-8")
                    .code(401)
                    .protocol(Protocol.HTTP_2)
                    .message("Not authorized")
                    .request(fakeRequest)
                    .body(tokenBody)
                    .build();

            // Hijack the return values for the http client
            when(httpClient.sendRequest(any())).thenReturn(fakeResponse);
        } catch (IOException e) {
            System.out.println("did not work");
            e.printStackTrace();
        }

        // Make the call
        String accessToken = service.requestToken();

        // Check that we have the access token we created for the response
        assertEquals("", accessToken);
    }



    /**
     * Tests that we are able to retrieve an access token upon request.
     */
    @Test
    public void getAccessTokenSuccess() {

        try {
            // We will fake a request for the access token,
            // which in return will yield a response.
            Request fakeRequest = createFakeRequest();

            // Create a dummy body for the response
            // This is what it will look like when returned from Vipps
            String tokenJSON = "{\n" +
                    "    \"token_type\": \"Bearer\",\n" +
                    "    \"expires_in\": \"86398\",\n" +
                    "    \"ext_expires_in\": \"86398\",\n" +
                    "    \"expires_on\": \"1591280038\",\n" +
                    "    \"not_before\": \"1591193339\",\n" +
                    "    \"resource\": \"00000002-0000-0000-c000-000000000000\",\n" +
                    "    \"access_token\": \"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1Ni\"\n" +
                    "}";

            // Put the token body into our fake response object
            ResponseBody tokenBody = ResponseBody.create(tokenJSON, MediaType.get("application/json"));

            // Create a fake response, containing the token content we would normally see in this situation
            Response fakeResponse = new Response.Builder()
                    .addHeader("content-type","application/json; charset=utf-8")
                    .code(200)
                    .protocol(Protocol.HTTP_2)
                    .message("")
                    .request(fakeRequest)
                    .body(tokenBody)
                    .build();

            // Hijack the return values for the http client
            when(httpClient.sendRequest(any())).thenReturn(fakeResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Make the call
        String accessToken = service.requestToken();

        // Check that we have the access token we created for the response
        assertEquals("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1Ni", accessToken);
    }

    /**
     * Test that we are able to handle a failure when fetching order status
     */
    @Test
    public void getOrderDetailsFail() {
        try {
            // We will fake the requests for these calls,
            // which in return will yield fake responses.
            Request fakeRequest = createFakeRequest();

            // Create a dummy body for the response
            // This is what it will look like when returned from Vipps
            String tokenJSON = "{\n" +
                    "    \"token_type\": \"Bearer\",\n" +
                    "    \"expires_in\": \"86398\",\n" +
                    "    \"ext_expires_in\": \"86398\",\n" +
                    "    \"expires_on\": \"1591280038\",\n" +
                    "    \"not_before\": \"1591193339\",\n" +
                    "    \"resource\": \"00000002-0000-0000-c000-000000000000\",\n" +
                    "    \"access_token\": \"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1Ni\"\n" +
                    "}";

            // Put the token body into our fake response object
            ResponseBody tokenBody = ResponseBody.create(tokenJSON, MediaType.get("application/json"));

            // Create a fake response, containing the token content we would normally see in this situation
            Response fakeTokenResponse = createFakeTokenResponse(3600, 1547823408, 1547819508);

            // Create a dummy body for the payment response
            String errorJSON = "{\n" +
                    "   \"errorGroup\": \"Payment\"," +
                    "   \"errorCode\": \"44\"," +
                    "   \"errorMessage\": \"Refused by issuer because of expired card\"," +
                    "   \"contextId\": \"f70b8bf7-c843-4bea-95d9-94725b19895f\"" +
                    "}";


            // Put the body into our fake response object
            ResponseBody errorBody = ResponseBody.create(errorJSON, MediaType.get("application/json"));

            // Create a fake response, containing a possible error from the server
            Response fakeErrorResponse = new Response.Builder()
                    .addHeader("content-type","application/json; charset=utf-8")
                    .code(400)
                    .protocol(Protocol.HTTP_2)
                    .message("")
                    .request(fakeRequest)
                    .body(errorBody)
                    .build();

            // Hijack the return values for the http client
            // First for fetching a token, then for calling the order status endpoint
            when(httpClient.sendRequest(any()))
                    .thenReturn(fakeTokenResponse)
                    .thenReturn(fakeErrorResponse);

            // Make the call
            String orderStatus = service.getOrderDetails("some_order_id");

            // Check that we have the access token we created for the response
            assertTrue(StringUtils.containsIgnoreCase(orderStatus, "errorCode"));
            assertTrue(StringUtils.containsIgnoreCase(orderStatus, "errorMessage"));

            // Check that we do make a call to the correct end point
            ArgumentCaptor<Request> argument = ArgumentCaptor.forClass(Request.class);
            verify(httpClient, times(2)).sendRequest(argument.capture());
            assertTrue(argument.getValue().url().toString().contains("/ecomm/v2/payments/some_order_id/details"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests successfully fetching an order's details
     */
    @Test
    public void getOrderDetailsSuccess() {
        try {
            // We will fake the requests for these calls,
            // which in return will yield fake responses.
            Request fakeRequest = createFakeRequest();

            // Create a fake response, containing the token content we would normally see in this situation
            Response fakeTokenResponse = createFakeTokenResponse(3600, 1547823408, 1547819508);

            // Create a dummy body for the payment response
            String orderJSON = "{\n" +
                    "   \"orderId\": \"order123abc\",\n" +
                    "   \"transactionSummary\": {\n" +
                    "           \"capturedAmount\": 20000,\n" +
                    "           \"refundedAmount\": 0,\n" +
                    "           \"remainingAmountToCapture\": 0,\n" +
                    "           \"remainingAmountToRefund\": 20000,\n" +
                    "           \"bankIdentificationNumber\": 123456\n" +
                    "   }\n"+
                    "}";


            // Put the body into our fake response object
            ResponseBody orderDetailsBody = ResponseBody.create(orderJSON, MediaType.get("application/json"));

            // Create a fake response, containing the order status we would normally see in this situation
            Response fakeDetailsResponse = new Response.Builder()
                    .addHeader("content-type","application/json; charset=utf-8")
                    .code(200)
                    .protocol(Protocol.HTTP_2)
                    .message("")
                    .request(fakeRequest)
                    .body(orderDetailsBody)
                    .build();

            // Hijack the return values for the http client
            // First for fetching a token, then for calling the order status endpoint
            when(httpClient.sendRequest(any()))
                    .thenReturn(fakeTokenResponse)
                    .thenReturn(fakeDetailsResponse);

            // Make the call
            String orderStatus = service.getOrderDetails("some_order_id");

            // Check that we have the access token we created for the response
            assertTrue(StringUtils.containsIgnoreCase(orderStatus, "order123abc"));
            assertTrue(StringUtils.containsIgnoreCase(orderStatus, "transactionSummary"));

            // Check that we do make a call to the correct end point
            ArgumentCaptor<Request> argument = ArgumentCaptor.forClass(Request.class);
            verify(httpClient, times(2)).sendRequest(argument.capture());
            assertTrue(argument.getValue().url().toString().contains("/ecomm/v2/payments/some_order_id/details"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void capturePaymentSuccess() {
        try {
            // We will fake the requests for these calls,
            // which in return will yield fake responses.
            Request fakeRequest = createFakeRequest();

            // Create a fake token response
            Response fakeTokenResponse = createFakeTokenResponse(3600, 1547823408, 1547819508);

            String responseJSON = "{\n" +
                    "   \"orderId\": \"order123abc\",\n" +
                    "   \"transactionInfo\": {\n" +
                    "       \"amount\": 20000,\n" +
                    "       \"status\": \"Captured\",\n" +
                    "       \"timeStamp\": \"2018-12-12T11:18:38.246Z\",\n" +
                    "       \"transactionId\": \"5001420062\",\n" +
                    "       \"transactionText\": \"One pair of Vipps socks\"\n" +
                    "       },\n" +
                    "   \"transactionSummary\": {\n" +
                    "       \"capturedAmount\": 20000,\n" +
                    "       \"refundedAmount\": 0,\n" +
                    "       \"remainingAmountToCapture\": 0,\n" +
                    "       \"remainingAmountToRefund\": 20000,\n" +
                    "       \"bankIdentificationNumber\": 123456\n" +
                    "   }\n" +
                    "}";

            // Create a fake response, containing the order status we would normally see in this situation
            Response fakeCaptureResponse = new Response.Builder()
                    .addHeader("content-type","application/json; charset=utf-8")
                    .code(200)
                    .protocol(Protocol.HTTP_2)
                    .message("")
                    .request(fakeRequest)
                    .body(ResponseBody.create(responseJSON, MediaType.get("application/json")))
                    .build();

            // Hijack the return values for the http client
            // First for fetching a token, then for calling the order status endpoint
            when(httpClient.sendRequest(any()))
                    .thenReturn(fakeTokenResponse)
                    .thenReturn(fakeCaptureResponse);

            // Make the call
            assertTrue(service.capturePayment("some_order_id", 200, "some transaction text"));

            // Check that we do make a call to the correct end point
            ArgumentCaptor<Request> argument = ArgumentCaptor.forClass(Request.class);
            verify(httpClient, times(2)).sendRequest(argument.capture());
            assertTrue(argument.getValue().url().toString().contains("/ecomm/v2/payments/some_order_id/capture"));

            // Check that the request is a PUT
            assertEquals("POST", argument.getValue().method());

            // Also verify that we send expected request body content
            RequestBody requestBody = argument.getValue().body();
            final Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            String result = buffer.readUtf8();
            assertTrue(result.contains("\"merchantInfo\":"));
            assertTrue(result.contains("merchantSerialNumber"));
            assertTrue(result.contains("\"transaction\":"));
            assertTrue(result.contains("\"amount\":"));
            assertTrue(result.contains("20000"));
            assertTrue(result.contains("transactionText"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void capturePaymentFail() {
        try {
            // We will fake the requests for these calls,
            // which in return will yield fake responses.
            Request fakeRequest = createFakeRequest();

            // Create a fake token response
            Response fakeTokenResponse = createFakeTokenResponse(3600, 1547823408, 1547819508);

            String responseJSON = "{\n" +
                    "   \"errorGroup\": \"Payment\",\n" +
                    "   \"errorCode\": \"44\",\n" +
                    "   \"errorMessage\": \"Refused by issuer because of expired card\",\n" +
                    "   \"contextId\": \"f70b8bf7-c843-4bea-95d9-94725b19895f\"";

            // Create a fake response, containing the order status we would normally see in this situation
            Response fakeCaptureResponse = new Response.Builder()
                    .addHeader("content-type","application/json; charset=utf-8")
                    .code(400)
                    .protocol(Protocol.HTTP_2)
                    .message("")
                    .request(fakeRequest)
                    .body(ResponseBody.create(responseJSON, MediaType.get("application/json")))
                    .build();

            // Hijack the return values for the http client
            // First for fetching a token, then for calling the order status endpoint
            when(httpClient.sendRequest(any()))
                    .thenReturn(fakeTokenResponse)
                    .thenReturn(fakeCaptureResponse);

            // Make the call
            assertFalse(service.capturePayment("some_order_id", 200, "some transaction text"));

            // Check that we do make a call to the correct end point
            ArgumentCaptor<Request> argument = ArgumentCaptor.forClass(Request.class);
            verify(httpClient, times(2)).sendRequest(argument.capture());
            assertTrue(argument.getValue().url().toString().contains("/ecomm/v2/payments/some_order_id/capture"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cancelOrderSuccess() {
        try {
            // We will fake the requests for these calls,
            // which in return will yield fake responses.
            Request fakeRequest = createFakeRequest();

            // Create a fake token response
            Response fakeTokenResponse = createFakeTokenResponse(3600, 1547823408, 1547819508);

            String responseJSON = "{\n" +
                    "   \"orderId\": \"order123abc\",\n" +
                    "   \"transactionInfo\": {\n" +
                    "       \"amount\": 20000,\n" +
                    "       \"status\": \"Cancelled\",\n" +
                    "       \"timeStamp\": \"2018-12-12T11:18:38.246Z\",\n" +
                    "       \"transactionId\": \"5001420062\",\n" +
                    "       \"transactionText\": \"One pair of Vipps socks\"\n" +
                    "       },\n" +
                    "   \"transactionSummary\": {\n" +
                    "       \"capturedAmount\": 20000,\n" +
                    "       \"refundedAmount\": 0,\n" +
                    "       \"remainingAmountToCapture\": 0,\n" +
                    "       \"remainingAmountToRefund\": 20000,\n" +
                    "       \"bankIdentificationNumber\": 123456\n" +
                    "   }\n" +
                    "}";

            // Create a fake response, containing the order status we would normally see in this situation
            Response fakeCancelResponse = new Response.Builder()
                    .addHeader("content-type","application/json; charset=utf-8")
                    .code(200)
                    .protocol(Protocol.HTTP_2)
                    .message("")
                    .request(fakeRequest)
                    .body(ResponseBody.create(responseJSON, MediaType.get("application/json")))
                    .build();

            // Hijack the return values for the http client
            // First for fetching a token, then for calling the order status endpoint
            when(httpClient.sendRequest(any()))
                    .thenReturn(fakeTokenResponse)
                    .thenReturn(fakeCancelResponse);

            // Make the call
            assertTrue(service.cancelOrder("some_order_id", "some transaction text"));

            // Check that we do make a call to the correct end point
            ArgumentCaptor<Request> argument = ArgumentCaptor.forClass(Request.class);
            verify(httpClient, times(2)).sendRequest(argument.capture());
            assertTrue(argument.getValue().url().toString().contains("/ecomm/v2/payments/some_order_id/cancel"));

            // Check that the request is a PUT
            assertEquals("PUT", argument.getValue().method());

            // Also verify that we send expected request body content
            RequestBody requestBody = argument.getValue().body();
            final Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            String result = buffer.readUtf8();
            assertTrue(result.contains("\"merchantInfo\":"));
            assertTrue(result.contains("merchantSerialNumber"));
            assertTrue(result.contains("\"transaction\":"));
            assertTrue(result.contains("transactionText"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cancelOrderFail() {
        try {
            // We will fake the requests for these calls,
            // which in return will yield fake responses.
            Request fakeRequest = createFakeRequest();

            // Create a fake token response
            Response fakeTokenResponse = createFakeTokenResponse(3600, 1547823408, 1547819508);

            String responseJSON = "{\n" +
                    "   \"errorGroup\": \"Payment\",\n" +
                    "   \"errorCode\": \"44\",\n" +
                    "   \"errorMessage\": \"Refused by issuer because of expired card\",\n" +
                    "   \"contextId\": \"f70b8bf7-c843-4bea-95d9-94725b19895f\"";

            // Create a fake response, containing the order status we would normally see in this situation
            Response fakeCancelResponse = new Response.Builder()
                    .addHeader("content-type","application/json; charset=utf-8")
                    .code(400)
                    .protocol(Protocol.HTTP_2)
                    .message("")
                    .request(fakeRequest)
                    .body(ResponseBody.create(responseJSON, MediaType.get("application/json")))
                    .build();

            // Hijack the return values for the http client
            // First for fetching a token, then for calling the order status endpoint
            when(httpClient.sendRequest(any()))
                    .thenReturn(fakeTokenResponse)
                    .thenReturn(fakeCancelResponse);

            // Make the call
            assertFalse(service.cancelOrder("some_order_id","some transaction text"));

            // Check that we do make a call to the correct end point
            ArgumentCaptor<Request> argument = ArgumentCaptor.forClass(Request.class);
            verify(httpClient, times(2)).sendRequest(argument.capture());
            assertTrue(argument.getValue().url().toString().contains("/ecomm/v2/payments/some_order_id/cancel"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

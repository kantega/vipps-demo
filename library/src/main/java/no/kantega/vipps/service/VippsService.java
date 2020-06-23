package no.kantega.vipps.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.kantega.vipps.HttpClient;
import no.kantega.vipps.config.IVippsClientConfig;
import no.kantega.vipps.dto.*;
import okhttp3.FormBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import okhttp3.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Vipps Service module holds methods for processing and maintaining payment transactions against Vipps.
 */
@Service
public class VippsService {

    @Autowired
    private Environment env;

    // Choose API url based on loaded test/prod profile!
    private final boolean isProduction = false; // Arrays.stream(env.getActiveProfiles()).anyMatch("prod"::equals);;
    private final String BASE_URL = isProduction ? "https://api.vipps.no" : "https://apitest.vipps.no";

    /** Holds the latest access token */
    private GetAccessTokenDTO accessTokenDTO;

    private final HttpClient httpClient;
    private final IVippsClientConfig vippsClientConfig;

    Logger logger = Logger.getLogger(VippsService.class.getName());

    @Autowired
    public VippsService(HttpClient httpClient,
                        IVippsClientConfig vippsClientConfig) {
        this.httpClient = httpClient;
        this.vippsClientConfig = vippsClientConfig;
    }

    /**
     * Returns a request token or an empty string.
     *
     * Prerequisites:
     * - A client ID is specified and found.
     * - A client secret is specified and found.
     * - An OCP APIM subscription key is specified and found.
     */
    public String requestToken(){
        if (accessTokenDTO == null || accessTokenDTO.hasExired()){
            FormBody body = new FormBody.Builder()
                    .build();

                Request request = new Request.Builder()
                    .header("client_Id", vippsClientConfig.getClientId())
                    .addHeader("client_Secret", vippsClientConfig.getClientSecret())
                    .addHeader("Ocp-Apim-Subscription-Key", vippsClientConfig.getEcomSubscriptionKey())
                    .url(BASE_URL + "/accesstoken/get")
                    .post(body)
                    .build();

            try {
                Response response = httpClient.sendRequest(request);

                if (response.isSuccessful()){
                    ResponseBody responseBody = response.body();
                    if (responseBody != null){
                        String responseBodyString = responseBody.string();
                        logger.info("accesstoken response from Vipps is successful and body is not null");
                        ObjectMapper objectMapper = new ObjectMapper();
                        accessTokenDTO = objectMapper.readValue(responseBodyString, GetAccessTokenDTO.class);
                    }
                    else {
                        logger.info("accesstoken response from Vipps is successful and body is null");
                        logger.info("accesstoken response from Vipps=" + response.message());
                    }
                }
                else {
                    logger.info("accesstoken response from Vipps is NOT successful");
                    logger.info("accesstoken response code from Vipps=" + response.code());

                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String responseBodyString = responseBody.string();
                        // Get error message from body
                        logger.info("accesstoken body is not null");
                        logger.info("accesstoken response message from Vipps=" + responseBodyString);
                    }
                    else {
                        logger.info("accesstoken body is null");
                        logger.info("accesstoken response message from Vipps=" + response.message());
                    }
                }

            } catch (IOException e) {
                logger.log(Level.SEVERE, "Caught exception: " + e.getMessage());
            }
        }

        return (accessTokenDTO != null) ? accessTokenDTO.getAccess_token() : "";
    }

    /**
     * Initiates a payment
     * @param mobileNumber customer mobile number
     * @param orderId transaction id
     * @param amount transaction amount in NOK
     * @param transactionText description of transaction
     * @return response for request as JSONString
     */
    public String createPayment(String mobileNumber, String orderId, double amount, String transactionText ) {

        CustomerInfoDTO customerInfoDTO = new CustomerInfoDTO(mobileNumber);
        MerchantInfoDTO merchantInfoDTO = new MerchantInfoDTO(vippsClientConfig.getMerchantSerialNo(), vippsClientConfig.getCallbackPrefix(), vippsClientConfig.getFallbackUrl());
        TransactionDTO transactionDTO = new TransactionDTO(orderId, amount, transactionText);

        InitiatePaymentRequestDTO initiatePaymentRequestDTO = new InitiatePaymentRequestDTO(customerInfoDTO, merchantInfoDTO, transactionDTO);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(initiatePaymentRequestDTO);

            RequestBody body = RequestBody.create(requestBody, MediaType.parse("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .addHeader("Authorization", "Bearer "+ requestToken())
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Ocp-Apim-Subscription-Key", vippsClientConfig.getEcomSubscriptionKey())
                    .url(BASE_URL+"/ecomm/v2/payments")
                    .post(body)
                    .build();

            Response response = httpClient.sendRequest(request);

            if (response.body() != null) {
                String responseBody = response.body().string();
                logger.info("Response from vipps: " + responseBody);

                return responseBody;
            }
        }
        catch (JsonProcessingException e) {
            logger.log(Level.SEVERE,"Caught exception: " + e.getMessage());
        }
        catch (IOException e) {
            logger.log(Level.SEVERE,"Caught exception: " + e.getMessage());
        }

        return "";
    }

    /**
     * Capture a reserved payment.
     * @param order_id The order id of the payment to capture.
     * @param amount The amount to capture - must be in NOK.
     * @param transactionText The transaction description provided upon payment.
     * @return true on success, otherwise false.
     * @throws IOException
     */
    public boolean capturePayment(String order_id, double amount, String transactionText) throws IOException {
        // Create a request body for this specific request
        String requestBody = "{\n" +
                "   \"merchantInfo\": {\n" +
                "       \"merchantSerialNumber\": " + vippsClientConfig.getMerchantSerialNo() + "\n" +
                "   },\n" +
                "   \"transaction\": {\n" +
                "       \"amount\": " + (amount * 100) + ",\n" + // The amount requested by Vipps is in Norwegian øre.
                "       \"transactionText\": \"" + transactionText + "\"\n" +
                "   }\n" +
                "}";

        RequestBody body = RequestBody.create(requestBody, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer "+ requestToken())
                .addHeader("Content-Type", "application/json")
                .addHeader("Ocp-Apim-Subscription-Key", vippsClientConfig.getEcomSubscriptionKey())
                .url(BASE_URL + "/ecomm/v2/payments/" + order_id + "/capture")
                .post(body)
                .build();

        Response response = httpClient.sendRequest(request);
        return response.isSuccessful();
    }

    /**
     * Requests the detail for an order.
     * @param order_id Id for the transaction
     * @return Response for the status request, as JSON
     */
    public String getOrderDetails(String order_id) throws IOException {

        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer "+ requestToken())
                .addHeader("Content-Type", "application/json")
                .addHeader("Ocp-Apim-Subscription-Key", vippsClientConfig.getEcomSubscriptionKey())
                .url(BASE_URL + "/ecomm/v2/payments/" + order_id + "/details")
                .build();

        Response response = httpClient.sendRequest(request);

        String responseString = "";

        if (response.isSuccessful()) {
            logger.info("order details returned success!");
            if (response.body() != null) {
                responseString = response.body().string();
                logger.info("order details body: " + responseString);
            }
            else {
                logger.info("order details body is null!");
            }

            // ObjectMapper objectMapper = new ObjectMapper();
            // OrderDetailsDTO orderDetails = objectMapper.readValue(responseString, OrderDetailsDTO.class);
        }
        else {
            logger.info("order details failed with error: " + response.code());

            if (response.body() != null) {
                responseString = response.body().string();
                logger.info("order details body: " + responseString);
            }
            else {
                logger.info("order details message: " + response.message());
            }
        }

        return responseString;
    }

    /**
     * Request to cancel an order.
     * @param order_id Identifies the order to cancel.
     * @return true if successful, otherwise false
     * @throws IOException
     */
    public boolean cancelOrder(String order_id, String transactionText) throws IOException {

        // Create a request body for this specific request
        String requestBody = "{\n" +
                "   \"merchantInfo\": {\n" +
                "       \"merchantSerialNumber\": " + vippsClientConfig.getMerchantSerialNo() + "\n" +
                "   },\n" +
                "   \"transaction\": {\n" +
                "       \"transactionText\": \"" + transactionText + "\"\n" +
                "   }\n" +
                "}";

        RequestBody body = RequestBody.create(requestBody, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer "+ requestToken())
                .addHeader("Content-Type", "application/json")
                .addHeader("Ocp-Apim-Subscription-Key", vippsClientConfig.getEcomSubscriptionKey())
                .url(BASE_URL + "/ecomm/v2/payments/" + order_id + "/cancel")
                .put(body)
                .build();

        Response response = httpClient.sendRequest(request);

        if (response.isSuccessful()) {
            logger.info("order details returned success!");
            if (response.body() != null) {
                logger.info("order details body: " + response.body().string());
            }
            else {
                logger.info("order details body is null!");
            }

            return true;
        }
        else {
            logger.info("order details failed with error: " + response.code());

            if (response.body() != null) {
                logger.info("order details body: " + response.body().string());
            }
            else {
                logger.info("order details message: " + response.message());
            }
        }

        return false;
    }
}

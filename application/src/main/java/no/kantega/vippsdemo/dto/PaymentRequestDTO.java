package no.kantega.vippsdemo.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the data used for requesting a payment.
 * Its content is typically used between the web client and the REST backend.
 */
public class PaymentRequestDTO {
    private String userId;
    private String paymentType; // Defaults to Vipps
    private String mobileNumber; // Can be empty
    private String transactionDescription; // Can be empty
    private List<ProductDTO> products;

    // For deserialization
    public PaymentRequestDTO() {
        this.paymentType = "Vipps";
        this.userId = "";
        this.products = new ArrayList<>();
        this.mobileNumber = "";
        this.transactionDescription = "";
    }

    public PaymentRequestDTO(String userId, List<ProductDTO> products) {
        this.paymentType = "Vipps";
        this.userId = userId;
        this.products = products;
        this.mobileNumber = "";
        this.transactionDescription = "";
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}

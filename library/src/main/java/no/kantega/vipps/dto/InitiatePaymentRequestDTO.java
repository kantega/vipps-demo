package no.kantega.vipps.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InitiatePaymentRequestDTO {

    @JsonProperty("customerInfo")
    private CustomerInfoDTO customerInfoDTO;

    @JsonProperty("merchantInfo")
    private MerchantInfoDTO merchantInfoDTO;

    @JsonProperty("transaction")
    private TransactionDTO transactionDTO;

    public InitiatePaymentRequestDTO() {
    }

    public InitiatePaymentRequestDTO(CustomerInfoDTO customerInfoDTO, MerchantInfoDTO merchantInfoDTO, TransactionDTO transactionDTO) {
        this.customerInfoDTO = customerInfoDTO;
        this.merchantInfoDTO = merchantInfoDTO;
        this.transactionDTO = transactionDTO;
    }

    public CustomerInfoDTO getCustomerInfoDTO() {
        return customerInfoDTO;
    }
    public void setCustomerInfoDTO(CustomerInfoDTO customerInfoDTO) {
        this.customerInfoDTO = customerInfoDTO;
    }
    public MerchantInfoDTO getMerchantInfoDTO() {
        return merchantInfoDTO;
    }
    public void setMerchantInfoDTO(MerchantInfoDTO merchantInfoDTO) {
        this.merchantInfoDTO = merchantInfoDTO;
    }
    public TransactionDTO getTransactionDTO() {
        return transactionDTO;
    }
    public void setTransactionDTO(TransactionDTO transactionDTO) {
        this.transactionDTO = transactionDTO;
    }

    @Override
    public String toString() {
        return "InitiatePaymentRequestDTO{" +
                "merchantInfoDTO=" + merchantInfoDTO +
                ", customerInfoDTO=" + customerInfoDTO +
                ", transactionDTO=" + transactionDTO +
                '}';
    }
}

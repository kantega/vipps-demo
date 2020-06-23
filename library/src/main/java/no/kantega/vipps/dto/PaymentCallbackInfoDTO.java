package no.kantega.vipps.dto;

public class PaymentCallbackInfoDTO {
    private String merchantSerialNumber;
    private String orderId;
    private CallbackTransactionInfoDTO transactionInfo;
    private CallbackErrorInfoDTO callbackErrorInfo;

    public PaymentCallbackInfoDTO() {
    }

    public String getMerchantSerialNumber() {
        return merchantSerialNumber;
    }

    public void setMerchantSerialNumber(String merchantSerialNumber) {
        this.merchantSerialNumber = merchantSerialNumber;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public CallbackTransactionInfoDTO getTransactionInfo() {
        return transactionInfo;
    }

    public void setTransactionInfo(CallbackTransactionInfoDTO transactionInfo) {
        this.transactionInfo = transactionInfo;
    }

    public CallbackErrorInfoDTO getCallbackErrorInfo() {
        return callbackErrorInfo;
    }

    public void setCallbackErrorInfo(CallbackErrorInfoDTO callbackErrorInfo) {
        this.callbackErrorInfo = callbackErrorInfo;
    }}

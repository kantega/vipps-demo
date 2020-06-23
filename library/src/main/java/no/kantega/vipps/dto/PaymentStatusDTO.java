package no.kantega.vipps.dto;

public class PaymentStatusDTO {
    private String merchantSerialNumber;
    private String orderId;
    private TransactionDTO transactionInfo;
    private ErrorDTO errorInfo;

    public PaymentStatusDTO() { }

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

    public TransactionDTO getTransactionInfo() {
        return transactionInfo;
    }

    public void setTransactionInfo(TransactionDTO transactionInfo) {
        this.transactionInfo = transactionInfo;
    }

    public ErrorDTO getCallbackErrorInfo() {
        return errorInfo;
    }

    public void setCallbackErrorInfo(ErrorDTO callbackErrorInfo) {
        this.errorInfo = callbackErrorInfo;
    }
}

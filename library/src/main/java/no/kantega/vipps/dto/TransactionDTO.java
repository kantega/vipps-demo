package no.kantega.vipps.dto;


public class TransactionDTO {
    private String orderId;
    private double amount;
    private String transactionText;

    public TransactionDTO(String orderId, double amount, String transactionText) {
        this.orderId = orderId;
        this.amount = amount;
        this.transactionText = transactionText;
    }

    public TransactionDTO() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionText() {
        return transactionText;
    }

    public void setTransactionText(String transactionText) {
        this.transactionText = transactionText;
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "orderId='" + orderId + '\'' +
                ", amount='" + amount + '\'' +
                ", transactionText='" + transactionText + '\'' +
                '}';
    }
}
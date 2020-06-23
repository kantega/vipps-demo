package no.kantega.vipps.dto;

public class OrderDetailsDTO {

    private String orderId;
    private TransactionSummaryDTO transactionSummary;

    public OrderDetailsDTO() {
    }

    public OrderDetailsDTO(String orderId, TransactionSummaryDTO transactionSummary) {
        this.orderId = orderId;
        this.transactionSummary = transactionSummary;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public TransactionSummaryDTO getTransactionSummary() {
        return transactionSummary;
    }

    public void setTransactionSummary(TransactionSummaryDTO transactionSummary) {
        this.transactionSummary = transactionSummary;
    }
}

package no.kantega.vipps.dto;

public class TransactionSummaryDTO {
    private double capturedAmount;
    private double refundedAmount;
    private double remainingAmountToCapture;
    private double remainingAmountToRefund;
    private String bankIdentificationNumber;

    public TransactionSummaryDTO() {
    }

    public TransactionSummaryDTO(double capturedAmount, double refundedAmount, double remainingAmountToCapture,
                                 double remainingAmountToRefund, String bankIdentificationNumber) {
        this.capturedAmount = capturedAmount;
        this.refundedAmount = refundedAmount;
        this.remainingAmountToCapture = remainingAmountToCapture;
        this.remainingAmountToRefund = remainingAmountToRefund;
        this.bankIdentificationNumber = bankIdentificationNumber;
    }

    public double getCapturedAmount() {
        return capturedAmount;
    }

    public void setCapturedAmount(double amount) {
        this.capturedAmount = amount;
    }

    public double getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(double amount) {
        this.refundedAmount = amount;
    }

    public double getRemainingAmountToCapture() {
        return remainingAmountToCapture;
    }

    public void setRemainingAmountToCapture(double amount) {
        this.remainingAmountToCapture = amount;
    }

    public double getRemainingAmountToRefund() {
        return remainingAmountToRefund;
    }

    public void setRemainingAmountToRefund(double amount) {
        this.remainingAmountToRefund = amount;
    }

    public String getBankIdentificationNumber() {
        return bankIdentificationNumber;
    }

    public void setBankIdentificationNumber(String number) {
        this.bankIdentificationNumber = number;
    }
}

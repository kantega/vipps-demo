package no.kantega.vippsdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VippsAccount {

    @JsonProperty("account_name")
    private String accountName;
    @JsonProperty("account_number")
    private String accountNumber;
    @JsonProperty("bank_name")
    private String bankName;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}

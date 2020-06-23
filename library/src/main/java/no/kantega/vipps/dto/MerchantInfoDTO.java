package no.kantega.vipps.dto;


public class MerchantInfoDTO {

    private String merchantSerialNumber;
    private String callbackPrefix;
    private String fallBack;


    public MerchantInfoDTO(String merchantSerialNumber, String callbackPrefix, String fallBack) {
        this.merchantSerialNumber = merchantSerialNumber;
        this.callbackPrefix = callbackPrefix;
        this.fallBack = fallBack;

    }

    public String getMerchantSerialNumber() {
        return merchantSerialNumber;
    }

    public void setMerchantSerialNumber(String merchantSerialNumber) {
        this.merchantSerialNumber = merchantSerialNumber;
    }

    public String getCallbackPrefix() {
        return callbackPrefix;
    }

    public void setCallbackPrefix(String callbackPrefix) {
        this.callbackPrefix = callbackPrefix;
    }

    public String getFallBack() {
        return fallBack;
    }

    public void setFallBack(String fallBack) {
        this.fallBack = fallBack;
    }


    @Override
    public String toString() {
        return "MerchantInfoDTO{" +
                "merchantSerialNumber='" + merchantSerialNumber + '\'' +
                ", callbackPrefix='" + callbackPrefix + '\'' +
                ", fallBack='" + fallBack + '\'' +
                '}';
    }
}
package no.kantega.vipps.dto;


public class CustomerInfoDTO {
    private String mobileNumber;

    public CustomerInfoDTO() {
    }

    public CustomerInfoDTO(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String toString() {
        return "CustomerInfoDTO{" +
                "mobileNumber='" + mobileNumber + '\'' +
                '}';
    }
}

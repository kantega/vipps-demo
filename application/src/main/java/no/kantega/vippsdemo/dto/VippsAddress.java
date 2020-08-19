package no.kantega.vippsdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VippsAddress {

    @JsonProperty("street_address")
    private String streetAddress;
    @JsonProperty("postal_code")
    private String postalCode;
    @JsonProperty("region")
    private String region;
    @JsonProperty("country")
    private String country;
    @JsonProperty("formatted")
    private String formatted;
    @JsonProperty("address_type")
    private String addressType;

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFormatted() {
        return formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }
}

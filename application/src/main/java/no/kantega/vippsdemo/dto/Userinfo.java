package no.kantega.vippsdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Userinfo {

    @JsonProperty("sub")
    private String sub;
    @JsonProperty("birthdate")
    private String birthdate;
    @JsonProperty("email")
    private String email;
    @JsonProperty("email_verified")
    private String emailVerified;
    @JsonProperty("nin")
    private String nin;
    @JsonProperty("name")
    private String name;
    @JsonProperty("given_name")
    private String givenName;
    @JsonProperty("family_name")
    private String familyName;
    @JsonProperty("sid")
    private String sid;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("address")
    private VippsAddress address;
    @JsonProperty("other_addresses")
    private VippsAddress[] otherAddresses;
    @JsonProperty("accounts")
    private VippsAccount[] accounts;

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(String emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public VippsAddress getAddress() {
        return address;
    }

    public void setAddress(VippsAddress address) {
        this.address = address;
    }

    public VippsAddress[] getOtherAddresses() {
        return otherAddresses;
    }

    public void setOtherAddresses(VippsAddress[] otherAddresses) {
        this.otherAddresses = otherAddresses;
    }

    public VippsAccount[] getAccounts() {
        return accounts;
    }

    public void setAccounts(VippsAccount[] accounts) {
        this.accounts = accounts;
    }
}

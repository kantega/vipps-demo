package no.kantega.vipps.dto;

public class CallbackErrorInfoDTO {
    private int errorCode;
    private String errorGroup;
    private String errorMessage;

    public CallbackErrorInfoDTO() {
    }

    public CallbackErrorInfoDTO(int errorCode, String errorGroup, String errorMessage) {
        this.errorCode = errorCode;
        this.errorGroup = errorGroup;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorGroup() {
        return errorGroup;
    }

    public void setErrorGroup(String errorGroup) {
        this.errorGroup = errorGroup;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

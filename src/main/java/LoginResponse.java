public class LoginResponse {
    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private Boolean result;
    private String message;

    public LoginResponse(Boolean result, String message) {
        this.result = result;
        this.message = message;
    }



}

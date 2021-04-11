package cnpm.doan.domain;

public class ResponeDomain {
    private Object data;
    private String message;
    private boolean success;

    public ResponeDomain(String messsage, boolean status) {
        this.message = messsage;
        this.success = status;
    }

    public ResponeDomain(Object object, String messsage, boolean success) {
        this.data = object;
        this.message = messsage;
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

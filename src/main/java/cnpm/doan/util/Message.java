package cnpm.doan.util;

public enum Message {
    EMAIL_EXISTED("Existed email"),
    SUBJECT_EMAIL_FORGOT_PASS("Thay đổi mật khẩu tài khoản Shapee Cloud"),
    INVALID_TOKEN("This token was accessed or expired "),
    EMAIL_NOT_FOUND("Email is not found"), ERROR_SENDING_EMAIL("Error while sending email"),
    CONTENT_EMAIL("Please check in your email"),
    USER_NOT_FOUND("Your password or email is incorrect!"),
    MISSING_UNAUTHORIZED("missing authorized to access this URL"),
    MISSING_ACCESS_TOKEN("Access Token is require"),
    EMPTY_RESULT("Empty result"),
    DATA_NOT_EXIST("Data not exist"),
    INVALID_DATE("Invalid date. Please choose another date"),
    SUCCESSFUlLY("Successfully"),
    INVALID_MANGER("Invalid manager's username"),
    INVALID_USER("Invalid user id in request"),
    INVALID_TOKEN_ACCESS("Invalid Token"),
    NOT_EXIST_MANAGER("Manager with request id is not exist"),
    PROJECT_NOT_DONE("This project has a task not done yet. Please make all tasks already done and delete again "),
    WAITING_ACCOUNT("Your Account registered successfully, wait for your admin check it or contact to Shappee's admin");
    private String detail;

    Message(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }
}

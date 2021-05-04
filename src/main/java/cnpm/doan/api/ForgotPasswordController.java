package cnpm.doan.api;

import cnpm.doan.domain.ResetPassDomain;
import cnpm.doan.domain.ResponeDomain;
import cnpm.doan.domain.UserDomain;
import cnpm.doan.entity.User;
import cnpm.doan.service.UserService;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.HTTPStatus;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.javamail.MimeMessageHelper;

import cnpm.doan.util.Message;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@CrossOrigin
@RestController
public class ForgotPasswordController {
    @Autowired
    private UserService userService;
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;

    @PostMapping("/forgot_password")
    public ResponseEntity processForgotPassword(@RequestParam  String email) {
        System.out.println(email);
        String token = RandomString.make(7);
        try {
            userService.updateResetPasswordToken(token, email);
//            String siteURL = request.getRequestURL().toString();
            sendEmail(email, token);
            return ResponseEntity.ok(new ResponeDomain(Message.CONTENT_EMAIL.getDetail(),Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
        } catch (CustormException ex) {
            return ResponseEntity.ok(new ResponeDomain(ex.getErrorType().getDetail(), false));
        } catch (UnsupportedEncodingException | MessagingException e) {
            return ResponseEntity.ok(new ResponeDomain(Message.ERROR_SENDING_EMAIL.getDetail(), HTTPStatus.fail));
        }
    }

    @GetMapping("/reset_password")
    public ResponseEntity showResetPasswordForm(@RequestParam(value = "token") String token) {
        User user = userService.findUserByResetPasswordToken(token);
        if (user == null) {
            return ResponseEntity.ok(new ResponeDomain(Message.INVALID_TOKEN.getDetail(), HTTPStatus.fail));
        }
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PostMapping("/reset_password")
    public ResponseEntity processResetPassword(@ModelAttribute ResetPassDomain resetPassDomain) {
        User user = userService.findUserByResetPasswordToken(resetPassDomain.getToken());
        if (user == null) {
            return ResponseEntity.ok(new ResponeDomain(Message.INVALID_TOKEN.getDetail(), HTTPStatus.success));
        } else {
            userService.updatePassword(user, resetPassDomain.getPassword());
            UserDomain userDomain = new UserDomain(user);
            return ResponseEntity.ok(userDomain);
        }
    }

    public void sendEmail(String recipientEmail, String link)
            throws javax.mail.MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(from);
        helper.setTo(recipientEmail);
        helper.setSubject(cnpm.doan.util.Message.SUBJECT_EMAIL_FORGOT_PASS.getDetail());
        String content = "<img src='https://scontent.fdad3-2.fna.fbcdn.net/v/t1.0-9/48384579_317001252241916_6859686426733182976_n.png?_nc_cat=101&ccb=1-3&_nc_sid=09cbfe&_nc_ohc=BuiQGHLlFAMAX-VNYVr&_nc_ht=scontent.fdad3-2.fna&oh=7e28303c598f2cc682e25c3ed10e1cd1&oe=606A0F6A'> "
                + "<br>"
                + "<p>Dear user.</p>"
                + "<br>"
                + "<p>We have received your reset password request</p>"
                + "<p>Please type below token to reset your password.</p>"
                + "<h5>" + link + "</h5>"
                + "<p>Ignore this email if you have remembered your password."
                + "<br>"
                + "Have a nice day.</p>";
        ;
        helper.setText(content, true);
        mailSender.send(message);
    }
}

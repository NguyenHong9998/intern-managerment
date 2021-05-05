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
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
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
    public ResponseEntity processForgotPassword(@RequestParam String email) {
        System.out.println(email);
        String token = RandomString.make(7);
        System.out.println("eeeeeeeeeeeeeeeeeeeeeee: "+ email);
        try {
            userService.updateResetPasswordToken(token, email);
//            String siteURL = request.getRequestURL().toString();
            sendEmail(email, token);
            return ResponseEntity.ok(new ResponeDomain(Message.CONTENT_EMAIL.getDetail(), Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
        } catch (CustormException ex) {
            return ResponseEntity.ok(new ResponeDomain(ex.getErrorType().getDetail(), false));
        } catch (UnsupportedEncodingException | MessagingException e) {
            return ResponseEntity.ok(new ResponeDomain(Message.ERROR_SENDING_EMAIL.getDetail(), HTTPStatus.fail));
        }
    }

    @PostMapping("/reset_password")
    public ResponseEntity processResetPassword(@ModelAttribute ResetPassDomain resetPassDomain) {
        User user = userService.findUserByResetPasswordToken(resetPassDomain.getToken());
        if (user == null) {
            return ResponseEntity.ok(new ResponeDomain(Message.INVALID_TOKEN.getDetail(), HTTPStatus.fail));
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
        String content = "<img src='https://res-1.cloudinary.com/crunchbase-production/image/upload/c_lpad,f_auto,q_auto:eco/yrunsh3clxeoqgfblejv'> "
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

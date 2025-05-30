package vn.duylamhust.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.duylamhust.jobhunter.domain.request.ContentEmailRequest;
import vn.duylamhust.jobhunter.service.EmailService;

@RestController
@RequiredArgsConstructor
public class EmailController extends BaseController {
    private final EmailService emailService;

    @GetMapping("/email")
    public void sendEmail(@RequestBody ContentEmailRequest request) {
        // emailService.sendEmailSync("lam2012003az@gmail.com", "test send email",
        // "<h1><b> Hello </b></h1>", false,
        // true);
        emailService.sendEmailFromTemplateSync(request);
    }

}

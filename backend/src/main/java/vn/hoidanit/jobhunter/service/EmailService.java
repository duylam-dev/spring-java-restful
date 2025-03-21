package vn.hoidanit.jobhunter.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import org.springframework.mail.MailException;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.request.ContentEmailRequest;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.ResumeRepository;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final ResumeRepository resumeRepository;
    private final JobRepository jobRepository;
//
//    public String sendEmail() {
//        var msg = new SimpleMailMessage();
//        msg.setTo("nanh78505@gmail.com");
//        msg.setSubject("Hello test project from Duy Lam");
//        msg.setText("Duy lam test spring");
//        mailSender.send(msg);
//        return "ok";
//    }

    public void sendEmailSync(String to, String subject, String content, boolean isMultipart,
            boolean isHtml) {
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
                    isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, isHtml);
            this.javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            System.out.println("ERROR SEND EMAIL: " + e);
        }
    }

    public void sendEmailFromTemplateSync(ContentEmailRequest request) {
        Context context = new Context();
        context.setVariable("candidateName", request.getCandidateName());
        context.setVariable("date", Instant.now().toString());
        context.setVariable("location", "HN");
        String content = this.templateEngine.process("job", context);
        this.sendEmailSync(request.getEmail(),"Interview Appointment Letter" , content, false, true);
    }
}

package com.linq.website.service;


import com.linq.website.dto.ContactUsDTO;
import com.linq.website.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Locale;

@Service
public class MailService {

    private static final String USER = "user";

    private static final String CONTACT = "contact";
    private static final String YEAR = "year";

    private static final String BASE_URL = "baseUrl";

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

//    @Value("${spring.base.url}")
    @Value("${website.main.url}")
    private String baseUrl;

    @Value("${spring.mail.fromMail}")
    private String fromMail;

    public MailService(
            @Autowired JavaMailSender javaMailSender,
            MessageSource messageSource,
            SpringTemplateEngine templateEngine
    ) {
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        this.sendEmailSync(to, subject, content, isMultipart, isHtml);
    }

    private void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(fromMail);
            message.setSubject(subject);
            message.setText(content, isHtml);
            System.out.println("Sending email to: " + to);
            javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        this.sendEmailFromTemplateSync(user, templateName, titleKey);
    }

    private void sendEmailFromTemplateSync(User user, String templateName, String titleKey) {
        if (user.getEmail() == null) {
            return;
        }
        Context context = new Context(Locale.ENGLISH);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl + "/api/v1/auth");
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, Locale.ENGLISH);
        this.sendEmailSync(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendActivationEmail(User user) {
        this.sendEmailFromTemplateSync(user, "/mail/activationEmail", "email.activation.title");
    }

    @Async
    public void sendCreationEmail(User user) {
        this.sendEmailFromTemplateSync(user, "/mail/creationEmail", "email.activation.title");
    }

    @Async
    public void sendPasswordResetMail(User user) {
        this.sendEmailFromTemplateSync(user, "/mail/passwordResetEmail", "email.reset.title");
    }

    @Async
    public void sendContactUsEnquiryMail(ContactUsDTO dto, User user) {
        this.sendEmailFromTemplateContact(dto, user, "/mail/contactUsSubmission", "contact.title");
    }

    private void sendEmailFromTemplateContact(ContactUsDTO dto, User user, String templateName, String titleKey) {
        if (user.getEmail() == null) {
            return;
        }
        Context context = new Context(Locale.ENGLISH);
        context.setVariable(USER, user);
        context.setVariable(YEAR, Calendar.getInstance().get(Calendar.YEAR));;
        context.setVariable(CONTACT, dto);
        context.setVariable(BASE_URL, baseUrl + "/api/v1/forms");
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, Locale.ENGLISH);
        this.sendEmailSync(user.getEmail(), subject, content, false, true);
    }
}


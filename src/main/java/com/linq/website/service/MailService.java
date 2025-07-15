package com.linq.website.service;


import com.linq.website.controller.WebController;
import com.linq.website.dto.ContactUsDTO;
import com.linq.website.entity.*;
import com.linq.website.enums.MailType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class MailService {

    private static final String USER = "user";
    private static final String PERSON = "PERSON";

    private static final String CONTACT = "contact";
    private static final String YEAR = "year";

    private static final String BASE_URL = "baseUrl";

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    private static final String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

    private static final Logger logger = LoggerFactory.getLogger(MailService.class); // Use logger

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

    protected void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(fromMail);
            message.setSubject(subject);
            message.setText(content, isHtml);
            logger.info("Sending email to: " + to + " From: "+fromMail);
            System.out.println("Sending email to: " + to+" From: "+fromMail);
            javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            logger.error("Sending email Exception [sendEmailSync]: "+e);
            throw new RuntimeException(e);
        }
    }

    protected void sendEmailFromTemplateSync(User user, String templateName, String titleKey) {
        if (user.getEmail() == null) {
            return;
        }
        Context context = new Context(Locale.ENGLISH);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl + "/api/v1/auth");
        context.setVariable(YEAR, currentYear);
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, Locale.ENGLISH);
        this.sendEmailSync(user.getEmail(), subject, content, false, true);
    }

    protected void sendEmailToManagement(Object dto, String email, String templateName, String titleKey, MailType mailType) {
        if (Optional.ofNullable(email).isEmpty()) {
            return;
        }

        Context context = new Context(Locale.ENGLISH);
        context.setVariable(YEAR, currentYear);

        switch (mailType) {
            case contact_form:
                context.setVariable(CONTACT, (ContactUsDTO)dto);
                break;

            case system_notification:
                context.setVariable(USER, (User)dto);
                break;

            case job_application:
                context.setVariable(PERSON, (JobApplication)dto);
                break;
        }
//        context.setVariable(BASE_URL, baseUrl + "/api/v1/forms");
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, Locale.ENGLISH);
        this.sendEmailSync(email, subject, content, false, true);
    }

}


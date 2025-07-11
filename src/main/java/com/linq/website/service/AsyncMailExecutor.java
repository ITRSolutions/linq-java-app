package com.linq.website.service;

import com.linq.website.dto.ContactUsDTO;
import com.linq.website.entity.JobApplication;
import com.linq.website.entity.User;
import com.linq.website.enums.MailType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncMailExecutor {

    @Autowired
    private MailService mailService;

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        mailService.sendEmailSync(to, subject, content, isMultipart, isHtml);
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        mailService.sendEmailFromTemplateSync(user, templateName, titleKey);
    }

    @Async
    public void sendActivationEmail(User user) {
        mailService.sendEmailFromTemplateSync(user, "mail/activationEmail", "email.activation.title");
    }

    @Async
    public void sendCreationEmail(User user) {
        mailService.sendEmailFromTemplateSync(user, "mail/creationEmail", "email.activation.title");
    }

    @Async
    public void sendPasswordResetMail(User user) {
        mailService.sendEmailFromTemplateSync(user, "mail/passwordResetEmail", "email.reset.title");
    }

    @Async
    public void sendContactUsEnquiryMail(ContactUsDTO dto, String email) {
        mailService.sendEmailToManagement(dto, email, "mail/contactUsEmail"
                , "contact.title", MailType.contact_form);
    }

    @Async
    public void sendResumeSubmittedMail(JobApplication obj, String email) {
        mailService.sendEmailToManagement(obj, email, "mail/ResumeSubmittedNotification"
                , "email.resumeSubmitted.title", MailType.job_application);
    }

    @Async
    public void sendNewUserRegisterEmail(User user, String email) {
        mailService.sendEmailToManagement(user, email, "mail/NewUserRegisterEmail",
                "email.registerUser.title", MailType.system_notification);
    }
}

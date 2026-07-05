package com.teamflow.service.impl;

import com.teamflow.entity.User;
import com.teamflow.service.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationServiceImpl.class);

    private final ObjectProvider<JavaMailSender> mailSenderProvider;

    @Value("${teamflow.mail.enabled:false}")
    private boolean mailEnabled;

    @Override
    public void send(User recipient, String subject, String message) {
        if (!mailEnabled) {
            log.info("Email notification skipped because teamflow.mail.enabled=false. recipient={}, subject={}", recipient.getEmail(), subject);
            return;
        }

        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        if (mailSender == null) {
            log.warn("Email notification skipped because no JavaMailSender is configured. recipient={}, subject={}", recipient.getEmail(), subject);
            return;
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipient.getEmail());
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }
}


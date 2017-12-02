package com.shakepoint.web.core.email.impl;

import com.shakepoint.email.model.Email;
import com.shakepoint.web.core.email.EmailAsyncSender;
import com.shakepoint.web.core.email.EmailSender;
import com.shakepoint.web.core.email.EmailSenderThread;
import com.shakepoint.web.core.email.Template;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collections;
import java.util.Map;

public class EmailAsyncSenderImpl implements EmailAsyncSender {

    private final Logger log = Logger.getLogger(getClass());

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Autowired
    private EmailSender emailSender;

    protected void sendEmail(String to, String subject, String templateName, Map<String, Object> params) {
        if(params == null)
            params = Collections.EMPTY_MAP;

        final String emailAsJson = new Email(to, templateName, subject, params).toJson();

        log.info("Sending email to "+to);

        executor.execute(new EmailSenderThread(emailAsJson, emailSender));
    }

    public void sendEmail(String to, Template emailTemplate, Map<String, Object> params) {
        sendEmail(to, emailTemplate.getSubject(), emailTemplate.getTemplateName(), params);
    }
}

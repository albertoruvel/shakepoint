package com.shakepoint.web.core.email.impl;

import com.shakepoint.email.EmailQueue;
import com.shakepoint.integration.jms.client.handler.JmsHandler;
import com.shakepoint.web.core.email.EmailSender;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class EmailSenderImpl implements EmailSender {

    private final Logger log = Logger.getLogger(getClass());

    @Autowired
    private JmsHandler jmsHandler;

    public void sendEmail(final String emailAsJson){
        if (Boolean.parseBoolean(System.getProperty("com.shakepoint.web.email.enabled"))) {
            jmsHandler.send(EmailQueue.NAME, emailAsJson);
        }else{
            log.info("Sending emails is disabled in standalone");
        }
    }
}

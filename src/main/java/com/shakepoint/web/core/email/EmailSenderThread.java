package com.shakepoint.web.core.email;

public class EmailSenderThread implements Runnable{

    private String emailAsJson;
    private EmailSender emailSender;

    public EmailSenderThread(String emailAsJson, EmailSender emailSender) {
        this.emailAsJson = emailAsJson;
        this.emailSender = emailSender;
    }

    public void run() {
        emailSender.sendEmail(emailAsJson);
    }
}

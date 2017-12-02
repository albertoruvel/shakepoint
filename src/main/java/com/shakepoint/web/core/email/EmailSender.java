package com.shakepoint.web.core.email;


public interface EmailSender {
    void sendEmail(final String emailAsJson);
}

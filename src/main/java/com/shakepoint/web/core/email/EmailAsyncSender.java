package com.shakepoint.web.core.email;

import java.util.Map;

public interface EmailAsyncSender {

    void sendEmail(String to, Template emailTemplate, Map<String, Object> params);
}

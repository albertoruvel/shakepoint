package com.shakepoint.web.core.email;

public enum Template {
    SIGNUP("successfully-signup","Registro en Shakepoint"),
    SUCCESSFULL_PURCHASE("success-purchase","Su compra ha sido procesada correctamente");

    Template(final String templateName, final String subject ){
        this.templateName = templateName;
        this.subject = subject;
    }

    private String templateName;
    private String subject;

    public String getTemplateName(){
        return this.templateName;
    }

    public String getSubject(){
        return this.subject;
    }
}

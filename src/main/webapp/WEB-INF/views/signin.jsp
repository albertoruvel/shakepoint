<%-- 
    Document   : signin
    Created on : Sep 18, 2015, 7:22:32 PM
    Author     : Alberto Rubalcaba
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/WEB-INF/views/includes.jsp"%>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="<c:url value="/resources/bower_components/jquery/jquery.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/resources/bower_components/material-design-lite/material.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/resources/bower_components/bootstrap/js/bootstrap.min.js" />"></script>
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
        <link rel="stylesheet" href="<c:url value="/resources/bower_components/bootstrap/css/bootstrap.min.css" />">
        <link rel="stylesheet" href="<c:url value="/resources/bower_components/material-design-lite/material.min.css" />">
        <link rel="stylesheet" href="<c:url value="/resources/css/main-stylesheet.css" />">
        <link rel="stylesheet" href="<c:url value="/resources/css/login-card.css" />">

        <title>ShakePoint</title>
    </head>
    <body>
        <div class="mdl-layout__container">
            <div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
                <jsp:include page='/WEB-INF/views/shared/shakepoint-menu.jsp'>
                    <jsp:param name="visible" value="1"/>
                </jsp:include>
                <main class="mdl-layout__content">
                    <div class="page-content text-center center-block">
                        <div class="mdl-card mdl-shadow--4dp center-block" style="width:60%;">
                            <div class="mdl-card__supporting-text text-center" style="width: 100%;">
                                <h4>Inicia sesión en ShakePoint</h4>
                            </div>
                            <div class="mdl-card__supporting-text" style="width: 100%;">
                                <form class="form-horizontal" name="loginForm" role="form" method="POST" action="<c:url value="/shakepoint_security_check"/>">
                                    <div class="form-group ">
                                        <div class="mdl-textfield mdl-js-textfield center">
                                            <input name="shakepoint_email" class="mdl-textfield__input center" id="femail" type="text" path="email" placeholder="Correo electrónico"/>
                                            <label class="mdl-textfield__label" for="femail" path="email"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="mdl-textfield mdl-js-textfield">
                                            <input name="shakepoint_password" class="mdl-textfield__input center" path="password" type="password"  id="fpass" placeholder="Contraseña"/>
                                            <label class="mdl-textfield__label" path="password" for="fpass" />
                                        </div>
                                    </div>
                                    <div class="row">
                                        <button type="submit" id="signInButton" class="mdl-button mdl-js-button mdl-button--colored" style="margin-bottom: 15px;">Iniciar</button>
                                    </div>
                                    <input type="hidden"
                                           name="${_csrf.parameterName}"
                                           value="${_csrf.token}"/>
                                </form>
                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger alert-dismissible" role="alert">
                                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                        <strong>Autenticacón fallida!</strong> ${error}
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>
    </body>
</html>

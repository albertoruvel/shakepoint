<%-- 
    Document   : new-technician
    Created on : Sep 20, 2015, 11:39:28 PM
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

        <title>Crear técnico</title>
        <style>
            .mdl-card{
                width: 60%; 
                height: 768px; 
            }
            .mdl-card__supporting-text{
                width: 100%;
            }
            .mdl-textfield{
                width: 70%;
            }
        </style>
    </head>
    <body>
        <div class="mdl-layout__container">
            <div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
                <jsp:include page='/WEB-INF/views/shared/shakepoint-menu.jsp'>
                    <jsp:param name="visible" value="1"/>
                </jsp:include>
                <main class="mdl-layout__content">
                    <div class="page-content text-center center-block">
                        <div class="mdl-card mdl-shadow--2dp center-block">
                            <div class="mdl-card__title center-block">
                                <h3>Crear técnico</h3>
                            </div>
                            <div class="mdl-card__supporting-text text-center">
                                <form name="new-technician-form" role="form" method="POST" action="<c:url value="new-technician"/>">
                                    <div class="form-group ">
                                        <div class="mdl-textfield mdl-js-textfield center">
                                            <input name="name" class="mdl-textfield__input center" id="name" type="text" path="name" placeholder="Nombre"/>
                                            <label class="mdl-textfield__label" for="name" path="name"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="mdl-textfield mdl-js-textfield">
                                            <input name="email" class="mdl-textfield__input center" path="email" type="text"  id="email" placeholder="Correo"/>
                                            <label class="mdl-textfield__label" path="email" for="email" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="mdl-textfield mdl-js-textfield">
                                            <input name="password" class="mdl-textfield__input center" path="password" type="password"  id="price" placeholder="Contraseña"/>
                                            <label class="mdl-textfield__label" path="password" for="password" />
                                        </div>
                                    </div>
                                    <div class="row">
                                        <button type="submit" id="signInButton" class="mdl-button mdl-js-button mdl-button--colored" style="margin-bottom: 15px;">Guardar</button>
                                    </div>
                                    <input type="hidden"
                                           name="${_csrf.parameterName}"
                                           value="${_csrf.token}"/>
                                </form>
                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger alert-dismissible" role="alert">
                                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                        <strong>Error!</strong> ${error}
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
<%-- 
    Document   : success
    Created on : Sep 19, 2015, 2:29:57 PM
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
        
        <title>ShakePoint</title>
    </head>
    <body>
        <div class="mdl-layout__container">
            <div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
                <jsp:include page='/WEB-INF/views/shared/shakepoint-menu.jsp'>
                        <jsp:param name="visible" value="0"/>
                </jsp:include>
                <main class="mdl-layout__content">
                    <div class="page-content text-center center-block">
                        <h3>Exito</h3>
                        <h4>${message}</h4>
                        <c:if test="${not empty newCombo}">
                        	<a href="product/${newCombo}/edit" class="mdl-button mdl-js-button mdl-button--accent center-block" >Elegir qué productos contiene el nuevo paquete</a>
                        </c:if>
                        <c:choose>
                            <c:when test="${message_code eq '1'}">
                                <%-- Machine Added --%>
                                <p>Si desea agregar productos a esta máquina, siga este <a href="<c:url value="/admin/machine/${machine_id}/products" />">link</a></p>
                            </c:when>
                        </c:choose>
                    </div>
                </main>
            </div>
        </div>
    </body>
</html>
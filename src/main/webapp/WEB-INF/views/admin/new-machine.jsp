<%-- 
    Document   : new-machine
    Created on : Sep 21, 2015, 5:52:18 PM
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
        <script type="text/javascript" src="<c:url value="/resources/js/new-machine.js" />"></script>
        <script async defer
                src="https://maps.googleapis.com/maps/api/js?key=AIzaSyANxIdXZhBxl8lMldomKdXAmhheGeV_wNc&callback=initMap">
        </script>
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
        <link rel="stylesheet" href="<c:url value="/resources/bower_components/bootstrap/css/bootstrap.min.css" />">
        <link rel="stylesheet" href="<c:url value="/resources/bower_components/material-design-lite/material.min.css" />">
        <link rel="stylesheet" href="<c:url value="/resources/css/main-stylesheet.css" />">

        <title>Nueva máquina</title>
        <style>
            #main-card{
                width: 60%; 
                //height: 768px; 
            }
            .mdl-textfield{
                width: 60%;
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
                        <div id="main-card" class="mdl-card mdl-shadow--2dp center-block">
                            <h4 class="text-center">Crear máquina</h4>
                            <c:choose>
                                <c:when test="${technicians.pageItems.isEmpty()}">
                                    <div class="alert alert-warning alert-dismissible" role="alert">
                                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                        <strong>Alerta!</strong> No hay técnicos registrados
                                        Puedes crear un técnico desde <a href="<c:url value="new-technician" />">aquí</a>
                                        La máquina puede crearse, pero no estará activa hasta que se le asigne un técnico
                                    </div>
                                </c:when>
                                <c:otherwise>

                                </c:otherwise>
                            </c:choose>
                            <form:form modelAttribute="machine" action="new-machine" method="POST" commandName="machine" id="new-machine-form">
                                <div class="form-group">
                                    <div class="mdl-textfield mdl-js-textfield">
                                        <input name="name" class="mdl-textfield__input" type="text" id="name" />
                                        <label class="mdl-textfield__label" for="name">Nombre de la máquina...</label>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="mdl-textfield mdl-js-textfield">
                                        <textarea name="description" class="mdl-textfield__input" type="text" id="description" rows="5"></textarea>
                                        <label class="mdl-textfield__label" for="description">Descripción de la máquina...</label>
                                    </div>
                                </div>
                                <input type="hidden"
                                       name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <div class="form-group">
                                    <c:choose>
                                        <c:when test="${! technicians.pageItems.isEmpty()}">
                                            <form:select path="technicianId" cssClass="mdl-shadow--2dp center-block" style="width:60%; height: 35px;border:none;" name="technicianId" >
                                                <form:option value="NONE" label="Select a technician" />
                                                <form:options items="${technicians.pageItems}" itemValue="id" itemLabel="name"/>
                                            </form:select>
                                        </c:when>
                                    </c:choose>

                                </div>
                                <div class="form-group">
                                    <div class="mdl-textfield mdl-js-textfield">
                                        <input path="location"  name="location" class="mdl-textfield__input" type="text" id="location" placeholder="Ubicación" readonly/>
                                    </div>
                                </div>
                                <h5>Arrastre el marcador a la ubicacón deseada de la máquina</h5>
                                <div style="height: 300px;width:100%;padding:15px;margin-bottom: 15px;">
                                    <div id="map" style="height: 100%;width:100%;">
                                    </div>
                                </div>
                                <button type="submit" class="mdl-button mdl-js-button mdl-js-ripple-effect mdl-button--colored">
                                    Guardar máquina
                                </button>
                            </form:form>
                        </div>
                    </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>

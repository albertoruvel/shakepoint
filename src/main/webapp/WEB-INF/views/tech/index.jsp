<%-- 
    Document   : index
    Created on : Sep 18, 2015, 7:23:35 PM
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
        <sec:csrfMetaTags/>
        <script type="text/javascript" src="<c:url value="/resources/bower_components/jquery/jquery.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/resources/bower_components/material-design-lite/material.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/resources/bower_components/bootstrap/js/bootstrap.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/resources/js/partner-index.js" />"></script>
        <script type="text/javascript" src="<c:url value="/resources/js/bootstrap-datepicker.js"/>"></script>
        <script src="https://code.highcharts.com/highcharts.js"></script>
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
        <link rel="stylesheet" href="<c:url value="/resources/bower_components/bootstrap/css/bootstrap.min.css" />">
        <link rel="stylesheet" href="<c:url value="/resources/bower_components/material-design-lite/material.min.css" />">
        <link rel="stylesheet" href="<c:url value="/resources/css/main-stylesheet.css" />">
        <link rel="stylesheet" href="<c:url value="/resources/css/bootstrap-datepicker.css" />">
        
        <title>ShakePoint</title>
        <style>
        	#main-card{
        		width: 60%;
        	}
        	.analytics{
                    		width: 100%;
                    		height: 428px;
                    		padding: 16px;
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
                        	<h5 id="user-name"></h5>
                        	<p style="margin-bottom:56px;">
                        		Número de máquinas con alerta: <b id="alerted-machines"></b><br/>
                        		Última vez que estuviste en ShakePoint: <b id="last-signin"></b>
                        	</p>


                        	<div class="input-daterange input-group" id="datepicker" style="padding-left:24px; padding-right: 24px;">
                                <input type="text" class="input-sm form-control" name="from" id="from-date-input"/>
                                <span class="input-group-addon"> a </span>
                                <input type="text" class="input-sm form-control" name="to" id="to-date-input"/>
                                <span class="input-group-addon">
                            		<button id="update-button" class="mdl-button mdl-js-button mdl-button--colored" style="font-size: 10px;">Actualizar</button>
                             	</span>
                            </div>
                            <div id="perMachineTotals" class="analytics center-block" style="margin-bottom: 56px;">

                            </div>

                            <h3 style="margin-bottom: 24px;">Cantidad de productos vendidos por vending</h3>
                            <div id="machines-data-container"></div>
                        </div>
                    </div>
                </main>
            </div>
        </div>
    </body>
</html>

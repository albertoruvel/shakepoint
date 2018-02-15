<%-- 
    Document   : index
    Created on : Sep 18, 2015, 7:23:27 PM
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
        <script type="text/javascript" src="<c:url value="http://ajax.aspnetcdn.com/ajax/knockout/knockout-3.3.0.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/resources/js/bootstrap-datepicker.js"/>"></script>
        <script src="https://code.highcharts.com/highcharts.js"></script>
        <script src="<c:url value="/resources/js/admin-index.js"/>"></script>
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
        <link rel="stylesheet" href="<c:url value="/resources/bower_components/bootstrap/css/bootstrap.min.css" />">
        <link rel="stylesheet" href="<c:url value="/resources/bower_components/material-design-lite/material.min.css" />">
        <link rel="stylesheet" href="<c:url value="/resources/css/main-stylesheet.css" />">
        <link rel="stylesheet" href="<c:url value="/resources/css/bootstrap-datepicker.css" />">
        <title>ShakePoint</title>
        <style>
        	#main-card{
        		width: 60%;
        		margin-bottom: 24px;
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
                        <div id="main-card" class="mdl-card mdl-shadow--2dp center-block" >
                        	<h5>Hola!</h5>
                        	<p>
                        		Número de máquinas activas: <b data-bind="text: activeMachines"></b><br/>
                        		Número de máquinas con alerta: <b data-bind="text: alertedMachines"></b><br/>
                        		Ventas el día de hoy: $<b data-bind="text: todayTotal"></b><br/>
                        		Número de técnicos registrados: <b data-bind="registeredTechnicians"></b>
                        	</p>
                        <p>
                            Esta opcion eliminara todo el contenido en Amazon S3 (use con cautela)
                        </p>
                        <button class="mdl-button mdl-js-button mdl-button--colored" data-bind="click: deleteMediaContent">Eliminar contenido en Amazon S3</button>
                        </div>
                        
                        <div id="main-card" class="mdl-card mdl-shadow--2dp center-block" style="padding: 16px; ">
                        		<div class="input-daterange input-group" id="datepicker">
    								<input type="text" class="input-sm form-control" name="from" data-bind="datepicker: perMachineFromValue"/>
    								<span class="input-group-addon"> a </span>
    								<input type="text" class="input-sm form-control" name="to" data-bind="datepicker: perMachineToValue"/>
    								<span class="input-group-addon"> 
										<a href="#" data-bind="click: updatePerMachineChart" style="font-size: 10px;">Actualizar</a>
 									</span>
								</div>
                        	<div id="perMachineTotals" class="analytics center-block">
                        		
                        	</div>
                        </div>
                        
                        <div id="main-card" class="mdl-card mdl-shadow--2dp center-block" >
                        	<div class="input-daterange input-group" id="datepicker">
    								<input type="text" class="input-sm form-control" name="from" data-bind="datepicker: totalIncomeFromValue"/>
    								<span class="input-group-addon"> a </span>
    								<input type="text" class="input-sm form-control" name="to" data-bind="datepicker: totalIncomeToValue"/>
    								<span class="input-group-addon"> 
										<a href="#" data-bind="click: updateTotalIncomeChart" style="font-size: 10px;">Actualizar</a>
 									</span>
								</div>
                        	<div id="totals" class="analytics center-block"></div>
                        </div>
                        
                        <div id="main-card" class="mdl-card mdl-shadow--2dp center-block" >
                        	<h5>Incidencia de fallas</h5>
                        	<div class="row">
                        		<div class="col-sm-6 col-md-6">
                        			<div style="width:100%;" class='input-group date'>
                    					<input ID="" type='text' class="form-control"/>
                    					<span class="input-group-addon">
                        				<span class="glyphicon glyphicon-calendar"></span>
                    					</span>
                					</div>
                        		</div>
                        		<div class="col-sm-6 col-md-6">
                        			<div style="width:100%;" class='input-group date'>
                    					<input type='text' class="form-control"/>
                    					<span class="input-group-addon">
                        				<span class="glyphicon glyphicon-calendar"></span>
                    					</span>
                					</div>
                        		</div>
                        	</div>
                        	<div id="fails" class="analytics center-block"></div>
                        </div>
                    </div>
                </main>
            </div>
        </div>
    </body>
</html>

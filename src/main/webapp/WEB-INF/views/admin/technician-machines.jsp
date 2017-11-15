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
        <script type="text/javascript"
	src="http://ajax.aspnetcdn.com/ajax/knockout/knockout-3.3.0.js"></script>
        <script type="text/javascript" src="<c:url value="/resources/js/technician-machines.js" />"></script>
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
        <link rel="stylesheet" href="<c:url value="/resources/bower_components/bootstrap/css/bootstrap.min.css" />">
        <link rel="stylesheet" href="<c:url value="/resources/bower_components/material-design-lite/material.min.css" />">
        <link rel="stylesheet" href="<c:url value="/resources/css/main-stylesheet.css" />">
        
        <title>ShakePoint</title>
        
        <style>
        	#main-card{
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
                <input type="hidden" id="tech_id" value="${technician_id}">
                    <div class="page-content text-center center-block">
                        <div id="main-card" class="mdl-card mdl-shadow--2dp center-block">
                        	<div class="mdl-card mdl-shadow--6dp center-block" style="width: 80%;">
                        		<h4>Información del técnico</h5>
                        		<h5 data-bind="text: technicianName"></h5>
                        		<h5 data-bind="text: technicianEmail"></h5>
                        		
                        	</div>
                        	
                        	<div class="row">
                        		<h5>Asigna máquinas al técnico con las tablas de abajo</h5>
                        		<div class="col-sm-6 col-md-6">
                        			<h5 data-bind="visible: allMachines().length == 0">No hay máquinas registradas, puedes agregar una desde <a href="<c:url value="/admin/new-machine"/>">aquí</a></h5>
                        			<table class="mdl-data-table mdl-js-data-table center-block mdl-shadow--4dp" style="width: 80%;" data-bind="visible: allMachines().length > 0">
                        				<thead>
                        					<tr>
                        						<th class="mdl-data-table__cell--non-numeric">Nombre</th>
                        						<th class="mdl-data-table__cell--non-numeric">Asignar</th>
                        					</tr>
                        				</thead>
                        				<tbody data-bind="foreach: {data: allMachines, as: 'machine'}">
                        					<tr>
                        						<td class="mdl-data-table__cell--non-numeric" data-bind="text: machine.name"></td>
                        						<td class="mdl-data-table__cell--non-numeric">
                        							<button class="mdl-button mdl-js-button mdl-button--ripple" data-bind="click: $parent.asignMachine">Asignar</button>
                        						</td>
                        					</tr>
                        				</tbody>
                        			</table>
                        		</div>
                        		<div class="col-sm-6 col-md-6">
                        		<h5 data-bind="visible: asignedMachines().length == 0">Asigna máquinas al técnico desde la tabla de la izquierda</h5>
                        		<table data-bind="visible: asignedMachines().length > 0" class="mdl-data-table mdl-js-data-table center-block mdl-shadow--4dp" style="width: 80%;">
                        				<thead>
                        					<tr>
                        						<th class="mdl-data-table__cell--non-numeric">Nombre</th>
                        						<th class="mdl-data-table__cell--non-numeric">Eliminar</th>
                        					</tr>
                        				</thead>
                        				<tbody data-bind="foreach: {data: asignedMachines, as: 'machine'}">
                        					<tr>
                        						<td class="mdl-data-table__cell--non-numeric" data-bind="text: machine.name"></td>
                        						<td class="mdl-data-table__cell--non-numeric">
                        							<button class="mdl-button mdl-js-button mdl-button--ripple" data-bind="click: $parent.removeMachine">Eliminar</button>
                        						</td>
                        					</tr>
                        				</tbody>
                        			</table>
                        		</div>
                        	</div>
                        </div>
                    </div>
                </main>
            </div>
        </div>
    </body>
</html>

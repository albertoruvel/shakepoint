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
        <style>
        	#main-card{
        		width: 60%; 
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
                        <div id="main-card" class="mdl-card mdl-shadow--4dp center-block" >
                        	<c:choose>
                        		<c:when test="${machines.pageItems.isEmpty()}">
                        			<h4>No tienes máquinas asignadas, pónte en contacto con el administrador del sistema.</h4>
                        		</c:when>
                        		<c:otherwise>
                        			<table class="mdl-data-table mdl-js-data-table center-block">
                        				<thead>
                        					<tr>
                        						<th class="mdl-data-table__cell--non-numeric">Nombre</th>
                        						<th class="mdl-data-table__cell--non-numeric">Descripción</th>
                        						<th class="mdl-data-table__cell--non-numeric">Alertas</th>
                        						<th class="mdl-data-table__cell--non-numeric">Productos</th>
                        						<th class="mdl-data-table__cell--non-numeric">Slots</th>
                        						<th class="mdl-data-table__cell--non-numeric">Registro</th>
                        					</tr>
                        				</thead>
                        				<tbody>
                        					<c:forEach var="machine" items="${machines.pageItems}">
                        						<tr>
                        							<td class="mdl-data-table__cell--non-numeric">${machine.name}</td>
                        							<td class="mdl-data-table__cell--non-numeric">${machine.description}</td>
                        							<td class="mdl-data-table__cell--non-numeric">
                        								<label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect" for="${machine.id}">
  															<input type="checkbox" id="${machine.id}" class="mdl-checkbox__input" checked="${machine.alerted}" readonly>
  															<%--<span class="mdl-checkbox__label">Checkbox</span>--%>
  															
														</label>	
                        							</td>
                        							<td class="mdl-data-table__cell--non-numeric">${machine.products} (<a href="#">Ver productos</a>)</td>
                        							<td class="mdl-data-table__cell--non-numeric">${machine.slots}</td>
                        							<td class="mdl-data-table__cell--non-numeric">
                        								<a href="<c:url value="machine/${machine.id}/log"/>">Ver registros</a>
                        							</td>
                        						</tr>
                        					</c:forEach>
                        				</tbody>
                        			</table>
                        		</c:otherwise>
                        	</c:choose>
                        </div>
                    </div>
                </main>
            </div>
        </div>
    </body>
</html>

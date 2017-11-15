<%-- 
    Document   : machine-products
    Created on : Sep 21, 2015, 10:46:01 PM
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
<sec:csrfMetaTags />
<script type="text/javascript"
	src="<c:url value="/resources/bower_components/jquery/jquery.min.js" />"></script>
<script type="text/javascript"
	src="<c:url value="/resources/bower_components/material-design-lite/material.min.js" />"></script>
<script type="text/javascript"
	src="<c:url value="/resources/bower_components/bootstrap/js/bootstrap.min.js" />"></script>
<script type="text/javascript"
	src="http://ajax.aspnetcdn.com/ajax/knockout/knockout-3.3.0.js"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/machine-products.js" />"></script>
<link rel="stylesheet"
	href="https://fonts.googleapis.com/icon?family=Material+Icons">
<link rel="stylesheet"
	href="<c:url value="/resources/bower_components/bootstrap/css/bootstrap.min.css" />">
<link rel="stylesheet"
	href="<c:url value="/resources/bower_components/material-design-lite/material.min.css" />">
<link rel="stylesheet"
	href="<c:url value="/resources/css/main-stylesheet.css" />">


<title>ShakePoint</title>
<style>
.mdl-card {
	width: 60%;
}

#main-card {
	width: 60%;
}

.row {
	padding-bottom: 16px;
	padding-top: 16px;
}

.mdl-card__supporting-text {
	padding: 5px;
	text-align: center;
}

.mdl-card__supporting-text {
	width: 100%;
}

.h5 {
	margin: 0px;
	text-align: start;
}
</style>
</head>
<body>
	<div class="mdl-layout__container">
		<input type="hidden" value="${machine_id}" id="machine_id">
		<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
			<jsp:include page='/WEB-INF/views/shared/shakepoint-menu.jsp'>
				<jsp:param name="visible" value="1" />
			</jsp:include>
			<main class="mdl-layout__content">
			<div class="page-content text-center center-block">
				<div id="main-card" class="mdl-card mdl-shadow--2dp center-block">
					<div class="row">
						<div class="col-md-8 col-sm-8">
							<div class="mdl-card mdl-shadow--4dp center-block"
								style="width: 80%; height: 100%;">
								<%-- machine info --%>
								<h5 data-bind="text: machineName"></h5>
								<p data-bind="text: machineDescription"></p>
								<p data-bind="visible: hasAlertedProducts">Ésta máquina tiene <b data-bind="text: alertedProducts"></b> productos con alerta</p>
							</div>
						</div>
						<div class="col-md-4 col-sm-8">
							<%-- technician info --%>
							<div class="mdl-card mdl-shadow--4dp center-block"
								style="width: 80%; height: 100%; padding: 16px;">
								<div data-bind="visible: technicianCard">
									<h5 data-bind="text: technicianName"></h5>
								<h5 data-bind="text:technicianEmail"></h5>
								<div class="mdl-card__menu">
									<button
										class="mdl-button mdl-button--icon mdl-js-button mdl-js-ripple-effect" data-bind="click: $root.deleteTechnician">
										<i class="material-icons">delete</i>
									</button>
								</div>
								</div>
								<div data-bind="visible: showAllTechnicians">
									<h5>Seleccione un técnico para la máquina</h5>
									<select style="width: 100%;height: 36px;" class="center-block" 
										data-bind="options: technicians, optionsText: 'name', value: selectedTechnician, optionsCaption: 'Seleccione un técnico', event: {change: $root.addNewTechnician}">
									
									</select>
								</div>
							</div>
							
						</div>
					</div>
					<div class="row">
						<h4 class="center-block">Puedes agregar productos. Todos
							serán creados con el 100% de disponibilidad</h4>
						<div class="col-md-6 col-sm-12">
							<%-- ALL PRODUCTS --%>
							<h4 data-bind="visible: allProducts().length == 0">
								No hay productos registrados<br />Agrega productos desde <a
									href="<c:url value="/admin/new-product" />">aquí</a>
							</h4>
							<table data-bind="visible: allProducts().length > 0"
								class="mdl-data-table mdl-js-data-table center-block">
								<thead>
									<tr>
										<th class="mdl-data-table__cell--non-numeric">Logo</th>
										<th class="mdl-data-table__cell--non-numeric">Nombre</th>
										<th class="mdl-data-table__cell--non-numeric">Slot</th>
										<th class="mdl-data-table__cell--non-numeric">Acción</th>
									</tr>
								</thead>
								<tbody data-bind="foreach: {data: allProducts, as: 'product'}">
									<tr>
										<td class="mdl-data-table__cell--non-numeric"
											style="display: flex; align-items: center;"><img data-bind="attr: {src: product.logoUrl}"
											width="50" height="40"></td>
										<td class="mdl-data-table__cell--non-numeric" data-bind="text: product.name"></td>
										<td class="mdl-data-table__cell--non-numeric"><input
											type="number" style="width: 30px;" data-bind="visible: product.combo == false, attr: {id: id, max: $root.maxSlots, min: $root.minSlots}"></td>
										<td class="mdl-data-table__cell--non-numeric">
											<button class="mdl-button mdl-js-button" data-bind="click: $parent.addProduct">Agregar</button>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
						<div class="col-md-6 col-sm-12">
							<%-- CURRENT MACHINE PRODUCTS --%>

							<h4 data-bind="visible: machineProducts().length == 0" class="center-block">No hay productos en esta maquina</h4>
							<h5 data-bind="visible: machineProducts().length == 0" class="center-block">Agrega productos desde la tabla de
								la izquierda</h5>


							<table data-bind="visible: machineProducts().length > 0" 
								class="mdl-data-table mdl-js-data-table center-block">
								<thead>
									<tr>
										<th class="mdl-data-table__cell--non-numeric">Logo</th>
										<th class="mdl-data-table__cell--non-numeric">Nombre</th>
										<th class="mdl-data-table__cell--non-numeric">Slot</th>
										<th class="mdl-data-table__cell--non-numeric">Acción</th>
									</tr>
								</thead>
								<tbody data-bind="foreach: {data: machineProducts, as: 'product'}">

									<tr>
										<td class="mdl-data-table__cell--non-numeric"
											style="display: flex; align-items: center;"><img width="50"
											height="40" data-bind="attr: {src: product.productLogoUrl}">
										</td>
										<td class="mdl-data-table__cell--non-numeric" data-bind="text: product.productName"></td>
										<td class="mdl-data-table__cell--non-numeric"><p data-bind="visible: product.combo == false, text: product.slotNumber"></p></td>
										<td class="mdl-data-table__cell--non-numeric">
											<button class="mdl-button mdl-js-button" data-bind="click: $parent.deleteProduct">Eliminar</button>
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


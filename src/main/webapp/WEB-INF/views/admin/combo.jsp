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
<sec:csrfMetaTags />
<script type="text/javascript"
	src="<c:url value="/resources/bower_components/jquery/jquery.min.js" />"></script>
<script type="text/javascript"
	src="<c:url value="/resources/bower_components/material-design-lite/material.min.js" />"></script>
<script type="text/javascript"
	src="<c:url value="/resources/bower_components/bootstrap/js/bootstrap.min.js" />"></script>
<script type="text/javascript"
	src="<c:url value="http://ajax.aspnetcdn.com/ajax/knockout/knockout-3.3.0.js"/>"></script>

<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="<c:url value="/resources/js/combo.js"/>"></script>
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
.main-card {
	width: 60%;
	margin-bottom: 24px;
}

.analytics {
	width: 100%;
	height: 428px;
	padding: 16px;
}

.row {
	margin: 0px 0px 16px 0px;
}
</style>
</head>
<body>
	<div class="mdl-layout__container">
		<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
			<jsp:include page='/WEB-INF/views/shared/shakepoint-menu.jsp'>
				<jsp:param name="visible" value="1" />
			</jsp:include>
			<main class="mdl-layout__content">
			<div class="page-content text-center center-block">
				<c:set var="hasError" value="${not empty error}" />
				<input type="hidden" value="${hasError}" id="hasError"> <input
					type="hidden" value="${productId}" id="productId">

				<div data-bind="visible: showAllElements" class="row">
					<div class="mdl-card mdl-shadow--2dp center-block main-card"><br/><br/>
						<p>
							Nombre del paquete: <b data-bind="text: combo().name"></b><br />
							Descripción del paquete: <b data-bind="text: combo().description"></b><br />
							Precio del paquete: <b data-bind="text: combo().price"></b><br />
							Total en producto: <b data-bind="text: total"></b><br/>
							Ahorro al comprar el producto: <b data-bind="text: saved"></b>
						</p><br/>
						<div class="row">
							<p>Crea un paquete agregando productos (izquierda) al paquete (derecha)<br/>
								Ten en cuenta que para tener disponibilidad del paquete, debes de contar<br>
								con los productos del paquete en la máquina a la que quieras agreagar este paquete.<br/>
								Puedes ver los productos que ofrece cada máquina desde <a href="<c:url value="/admin/machines"/>">aquí</a>
							</p>
							<div class="col-sm-6 col-md-6">
								<h5 data-bind="visible: products().length == 0">No hay productos registrados, puedes agreagr desde <a href="<c:url value="/admin/new-product"/>">aquí</a></h5>
								<h5 data-bind="visible: products().length > 0">Productos registrados</h5>
								<table data-bind="visible: products().length > 0" class="mdl-data-table mdl-js-data-table center-block">
									<thead>
										<tr>
											<th class="mdl-data-table__cell--non-numeric">Nombre</th>
											<th class="mdl-data-table__cell--non-numeric">Precio</th>
											<th class="mdl-data-table__cell--non-numeric">Acción</th>
										</tr>
									</thead>
									<tbody data-bind="foreach: { data: products, as: 'product' }">
										<tr>
											<td data-bind="text: product.name"
												class="mdl-data-table__cell--non-numeric"></td>
											<td data-bind="text: product.price"
												class="mdl-data-table__cell--non-numeric"></td>
											<td class="mdl-data-table__cell--non-numeric">
												<button data-bind="click: $parent.addProduct" class="mdl-button mdl-js-button mdl-button--accent">Agregar</button>
											</td>
										</tr>
									</tbody>
								</table>

							</div>
							<div class="col-sm-6 col-md-6">
								<h5 data-bind="visible: comboProducts().length == 0">Este paquete no contiene productos ligados, agrégalos desde la tabla izquierda</h5>
								<h5 data-bind="visible: comboProducts().length > 0">Productos en el paquete</h5>
								<table data-bind="visible: comboProducts().length > 0" class="mdl-data-table mdl-js-data-table center-block">
									<thead>
										<tr>
											<th class="mdl-data-table__cell--non-numeric">Nombre</th>
											<th class="mdl-data-table__cell--non-numeric">Precio</th>
											<th class="mdl-data-table__cell--non-numeric">Acción</th>
										</tr>
									</thead>
									<tbody data-bind="foreach: { data: comboProducts, as: 'product' }">
										<tr>
											<td data-bind="text: product.name"
												class="mdl-data-table__cell--non-numeric"></td>
											<td data-bind="text: product.price"
												class="mdl-data-table__cell--non-numeric"></td>
											<td class="mdl-data-table__cell--non-numeric">
												<button data-bind="click: $parent.removeProduct" class="mdl-button mdl-js-button mdl-button--accent">Quitar</button>
											</td>
										</tr>
									</tbody>
								</table>

							</div>

						</div>
					</div>

				</div>
			</div>
			</main>
		</div>
	</div>
</body>
</html>

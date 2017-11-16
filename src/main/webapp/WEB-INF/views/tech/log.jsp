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
#main-card {
	width: 60%;
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
				<div id="main-card" class="mdl-card mdl-shadow--2dp center-block">
					<c:choose>
						<c:when test="${logs.pageItems.isEmpty()}">
							<h4>
								Esta máquina no contiene mensajes de registro
							</h4>
						</c:when>
						<c:otherwise>
							<table class="mdl-data-table mdl-js-data-table center-block" style="width: 80%;">
								<thead>
									<tr>
										<th class="mdl-data-table__cell--non-numeric">Fecha</th>
										<th class="mdl-data-table__cell--non-numeric">Mensaje</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${logs}" var="machine">
										<tr>
											<td class="mdl-data-table__cell--non-numeric">${logs.failDate}</td>
											<td class="mdl-data-table__cell--non-numeric">${logs.message}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
							<button class="mdl-button mdl-js-button mdl-button--accent center-block">Borrar todos los registros para esta máquina</button>
							<%--<c:if test="${logs.pageNumber < logs.pagesAvailable}">
								<ul class="pager">
									<c:set var="previous" scope="request"
										value="?page_number=${logs.pageNumber - 1}" />
									<c:set var="next" scope="request"
										value="?page_number=${logs.pageNumber + 1}" />
									<c:choose>
										<c:when test="${logs.pageNumber == 1}">
											<li id="disabledPrevious" class="disabled"><a href="#">Previous</a></li>
											<div class="mdl-tooltip" for="disabledPrevious">There
												is no page 0</div>
										</c:when>
										<c:otherwise>
											<li><a href="${previous}">Previous</a></li>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when
											test="${logs.pageNumber == logs.pagesAvailable}">
											<li id="disabledNext" class="disabled"><a href="#">Next</a></li>
											<div class="mdl-tooltip" for="disabledNext">No more
												pages</div>
										</c:when>
										<c:otherwise>
											<li><a href="${next}">Next</a></li>
										</c:otherwise>
									</c:choose>
								</ul>
							</c:if>--%>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			</main>
		</div>
	</div>
</body>
</html>
l>

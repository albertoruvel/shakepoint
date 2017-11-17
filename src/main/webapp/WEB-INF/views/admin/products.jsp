<%-- 
    Document   : products
    Created on : Sep 19, 2015, 12:22:01 PM
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
<link rel="stylesheet"
	href="<c:url value="/resources/css/product-card.css" />">

<title>ShakePoint</title>
<style>
#main-card {
	width: 60%;
	height: 768px;
	padding: 16px;
}

.product-mdl-card {
	height: 20%;
}

.mdl-card__menu {
	top: 0px;
}

.row {
	height: 128px;
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
			<div class="page-content center-block">
				<div id="main-card" class="mdl-card mdl-shadow--2dp center-block">
					<c:choose>
						<c:when test="${products.isEmpty()}">
							<h4>No hay productos registrados</h4>
							<a href="<c:url value="new-product"/>"
                            					class="new-item mdl-button mdl-js-button mdl-button--fab mdl-js-ripple-effect mdl-button--colored">
                            					<i class="material-icons">add</i>
                            				</a>
						</c:when>
						<c:otherwise>
							<c:forEach items="${products}" var="product"
								varStatus="i">
								<%-- add a new card --%>
								<c:choose>
									<c:when test="${(i.index % 2) == 0}">
										<div class="row">
											<%-- first element --%>
											<div class="col-md-6">
												<jsp:include page="/WEB-INF/views/shared/product-card.jsp">
													<jsp:param name="id" value="${product.id}" />
													<jsp:param name="name" value="${product.name}" />
													<jsp:param name="price" value="${product.price}" />
													<jsp:param name="logoUrl" value="${product.logoUrl}" />
													<jsp:param name="combo" value="${product.type}" />
													<jsp:param name="creationDate"
														value="${product.creationDate}" />
													<jsp:param name="location" value="left" />
												</jsp:include>
											</div>
											<%-- Check if this is the last element --%>
											<c:if test="${(i.index + 1) == products.size()}">
										</div>
										</c:if>
									</c:when>
									<c:otherwise>
										<div class="col-md-6">
											<jsp:include page="/WEB-INF/views/shared/product-card.jsp">
												<jsp:param name="id" value="${product.id}" />
												<jsp:param name="name" value="${product.name}" />
												<jsp:param name="price" value="${product.price}" />
												<jsp:param name="logoUrl" value="${product.logoUrl}" />
												<jsp:param name="combo" value="${product.type}" />
												<jsp:param name="creationDate"
													value="${product.creationDate}" />
												<jsp:param name="location" value="right" />
											</jsp:include>
										</div>
				</div>
				</c:otherwise>
				</c:choose>
				</c:forEach>
				<a href="<c:url value="new-product"/>"
					class="new-item mdl-button mdl-js-button mdl-button--fab mdl-js-ripple-effect mdl-button--colored">
					<i class="material-icons">add</i>
				</a>

				<%--
				<c:if test="${products.pageNumber < products.pagesAvailable}">
					<ul class="pager">
						<c:set var="previous" scope="request"
							value="?page_number=${products.pageNumber - 1}" />
						<c:set var="next" scope="request"
							value="?page_number=${products.pageNumber + 1}" />

						<c:choose>
							<c:when test="${products.pageNumber == 1}">
								<li id="disabledPrevious" class="disabled"><a href="#">Previous</a></li>
								<div class="mdl-tooltip" for="disabledPrevious">There is
									no page 0</div>
							</c:when>
							<c:otherwise>
								<li><a href="${previous}">Previous</a></li>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${products.pageNumber == products.pagesAvailable}">
								<li id="disabledNext" class="disabled"><a href="#">Next</a></li>
								<div class="mdl-tooltip" for="disabledNext">No more pages</div>
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

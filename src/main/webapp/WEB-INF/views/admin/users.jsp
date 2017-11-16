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
                        <div id="main-card" class="mdl-cardmdl-shadow--4dp center-block">
                        	<h4>Usuarios de ShakePoint</h4>
                        	<table class="mdl-data-table mdl-js-data-table center-block mdl-shadow--4dp" style="width: 80%;">
                        		<thead>
                        			<tr>
                        				<th class="mdl-data-table__cell--non-numeric">Nombre</th>
                        				<th class="mdl-data-table__cell--non-numeric">Email</th>
                        				<th class="mdl-data-table__cell--non-numeric">Total en compras</th>
                        			</tr>
                        		</thead>
                        		
                        		<tbody>
                        			<c:forEach var="user" items="${users}">
                        				<tr>
                        					<td class="mdl-data-table__cell--non-numeric">${user.name}</td>
                        					<td class="mdl-data-table__cell--non-numeric">${user.email}</td>
                        					<td class="mdl-data-table__cell--non-numeric">${user.purchasesTotal}</td>
                        				</tr>
                        			</c:forEach>
                        		</tbody>
                        	</table>


                        	<%--<c:if test="${users.pageNumber < users.pagesAvailable}">
					<ul class="pager">
						<c:set var="previous" scope="request"
							value="?page_number=${users.pageNumber - 1}" />
						<c:set var="next" scope="request"
							value="?page_number=${users.pageNumber + 1}" />

						<c:choose>
							<c:when test="${users.pageNumber == 1}">
								<li id="disabledPrevious" class="disabled"><a href="#">Previous</a></li>
								<div class="mdl-tooltip" for="disabledPrevious">There is
									no page 0</div>
							</c:when>
							<c:otherwise>
								<li><a href="${previous}">Previous</a></li>
							</c:otherwise>
						</c:choose>

						<c:choose>
							<c:when test="${users.pageNumber == users.pagesAvailable}">
								<li id="disabledNext" class="disabled"><a href="#">Next</a></li>
								<div class="mdl-tooltip" for="disabledNext">No more pages</div>
							</c:when>
							<c:otherwise>
								<li><a href="${next}">Next</a></li>
							</c:otherwise>
						</c:choose>
					</ul>
				</c:if>--%>
                        </div>
                    </div>
                </main>
            </div>
        </div>
    </body>
</html>

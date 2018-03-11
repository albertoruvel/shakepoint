<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/WEB-INF/views/includes.jsp"%>
<html>
    <head>
        <sec:csrfMetaTags/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="<c:url value="/resources/bower_components/jquery/jquery.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/resources/bower_components/material-design-lite/material.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/resources/bower_components/bootstrap/js/bootstrap.min.js" />"></script>
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
        <link rel="stylesheet" href="<c:url value="/resources/bower_components/bootstrap/css/bootstrap.min.css" />">
        <link rel="stylesheet" href="<c:url value="/resources/bower_components/material-design-lite/material.min.css" />">
        <link rel="stylesheet" href="<c:url value="/resources/css/main-stylesheet.css" />">
        <script type="text/javascript" src="<c:url value="/resources/js/partner-order.js" />"></script>
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
                <input type="hidden" id="machineId" value="${products.machineId}"/>
                    <div class="page-content text-center center-block">
                        <div id="main-card" class="mdl-card mdl-shadow--4dp center-block" >
                        	<c:choose>
                        		<c:when test="${products.products.isEmpty()}">
                        			<h4>Esta máquina no contiene productos disponibles.</h4>
                        		</c:when>
                        		<c:otherwise>
                        			    <table class="mdl-data-table mdl-js-data-table" style="width: 100%; margin-bottom: 24px;">
                                             <thead style="width: 100%;">
                                                <tr>
                                                    <th class="mdl-data-table__cell--non-numeric"></th>
                                                    <th class="mdl-data-table__cell--non-numeric">Nombre</th>
                                                    <th class="mdl-data-table__cell--non-numeric">Alertas?</th>
                                                    <th class="mdl-data-table__cell--non-numeric">Cantidad</th>
                                                </tr>
                                             </thead>
                                             <tbody>
                                                <c:forEach var="p" items="${products.products}">
                                                    <tr>
                                                    	<td class="mdl-data-table__cell--non-numeric" style="padding-bottom: 24px;"><img src="${p.imageUrl}" width="60px" height="60px"/></td>
                                                    	<td class="mdl-data-table__cell--non-numeric" style="padding-top:24px;">
                                                    	    <div>
                                                    	        ${p.productName}
                                                    	    </div>
                                                    	</td>
                                                    	<td class="mdl-data-table__cell--non-numeric" style="padding-top:24px;">
                                                            <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect">
                                                          	    <input type="checkbox" id="${p.productId}" class="mdl-checkbox__input" "${p.alerted ? 'checked' : ''}">
                                                        	</label>
                                                            </td>
                                                    	<td class="mdl-data-table__cell--non-numeric" style="padding-top:24px;"><input class="product-quantity" type="number" id="${p.productId}" value="${p.requestedQuantity}"></td>
                                                    </tr>
                                                </c:forEach>
                                             </tbody>
                                        </table>
                                        <button class="mdl-button mdl-js-button mdl-button--colored" id="create-order"  type="button">Crear orden</button>
                        		</c:otherwise>
                        	</c:choose>
                        </div>
                    </div>
                </main>
            </div>
        </div>
    </body>
</html>

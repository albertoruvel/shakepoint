<%-- 
    Document   : shakepoint-menu
    Created on : Sep 18, 2015, 9:33:38 PM
    Author     : Alberto Rubalcaba
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<style>              

</style>
<header class="mdl-layout__header">
    <div class="mdl-layout__header-row">
        <!-- Title -->
        <a href="<c:url value="/" />" style="color: white;"><span class="mdl-layout-title">ShakePoint</span></a>
        <!-- Add spacer, to align navigation to the right -->
        <div class="mdl-layout-spacer"></div>
        <!-- Navigation. We hide it in small screens. -->
        <nav class="mdl-navigation mdl-layout--large-screen-only">
        <c:url value="/shakepoint_security_logout" var="logoutUrl" />
            <c:choose>
                <c:when test="${param.visible == 1}">
                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                <a class="mdl-navigation__link" style="color: white;" href="<c:url value="/admin/products" />">Productos</a>
                <%--<a class="mdl-navigation__link" style="color: white;" href="<c:url value="technicians" />">Technicians</a>--%>
                <a class="mdl-navigation__link" style="color: white;" href="<c:url value="/admin//machines" />">M�quinas</a>
                <form id="logout" action="${logoutUrl}" method="post" >
  					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
  					<button onclick="logout();" class="mdl-navigation__link" style="color: white;background-color: transparent;border: 0px;">Salir</button>
				</form>
            </sec:authorize>
            <sec:authorize access="hasRole('ROLE_SUPER_ADMIN')">
                <a class="mdl-navigation__link" style="color: white;" href="<c:url value="/admin/products" />">Productos</a>
                <a class="mdl-navigation__link" style="color: white;" href="<c:url value="/admin/technicians" />">T�cnicos</a>
                <a class="mdl-navigation__link" style="color: white;" href="<c:url value="/admin/machines" />">M�quinas</a>
                <a class="mdl-navigation__link" style="color: white;" href="<c:url value="/admin/users" />">Usuarios</a>
                <form id="logout" action="${logoutUrl}" method="post" >
  					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
  					<button onclick="logout();" class="mdl-navigation__link" style="color: white;background-color: transparent;border: 0px;">Salir</button>
				</form>
            </sec:authorize>
            <sec:authorize access="hasRole('ROLE_TECHNICIAN')">
            <a class="mdl-navigation__link" style="color: white;" href="<c:url value="/tech/machines" />">Mis M�quinas</a>
                <%--<a class="mdl-navigation__link" style="color: white;" href="<c:url value="/tech/fails" />">Fallas</a>
                <a class="mdl-navigation__link" style="color: white;" href="<c:url value="/tech/alerts" />">Alertas</a>--%>
                <form id="logout" action="${logoutUrl}" method="post" >
  						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
  						<button onclick="logout();" class="mdl-navigation__link" style="color: white;background-color: transparent;border: 0px;">Salir</button>
					</form>
            </sec:authorize>
            <%--<sec:authorize access="hasRole('ROLE_MEMBER')">
                <a class="mdl-navigation__link" href="">Link</a>
                <a class="mdl-navigation__link" href="">Link</a>
                <a class="mdl-navigation__link" href="">Link</a>
                <a class="mdl-navigation__link" href="">Link</a>  
            </sec:authorize>--%>
            <sec:authorize access="isAnonymous()">
                <a class="mdl-navigation__link" style="color: white;" href="<c:url value="signin"/>">Iniciar</a>
                <%--<a class="mdl-navigation__link" href="<c:url value="apply" />">Apply</a>--%>
            </sec:authorize>
                </c:when>
                <c:otherwise>
                    <a class="mdl-navigation__link" style="color:white;" href="<c:url value="/" />">Home</a>
                </c:otherwise>
            </c:choose>
        </nav>
    </div>
</header>
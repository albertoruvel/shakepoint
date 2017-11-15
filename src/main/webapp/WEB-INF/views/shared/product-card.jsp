<%-- 
    Document   : product-card
    Created on : Sep 19, 2015, 3:47:51 PM
    Author     : Alberto Rubalcaba
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
    <c:when test="${param.location eq 'left'}">
        <div class="mdl-card mdl-shadow--6dp center-block" style="margin-right: 10px;">
            <div class="row">
                <div class="col-md-3 col-sm-4" style="display:flex;align-items:center;"><img class="img-circle img-responsive" src="${param.logoUrl}"></div>
                <div class="col-md-9 col-sm-8">
                    <div class="mdl-card__title">${param.name}</div>
                    <div class="mdl-card__supporting-text"> 
                        <p><strong>Precio </strong> ${param.price} MXN <!--<a id="${param.id}" style="cursor: default;">edit</a>--><br/>
                            <strong>Creado en </strong> ${param.creationDate}
                        </p>
                    </div>
                    <c:if test="${param.combo == true}">
                        	<div class="mdl-card__menu">
    							<a href="<c:url value="product/${param.id}/edit"/>" class="mdl-button mdl-js-button mdl-button--icon">
    								<i class="material-icons">edit</i>
    							</a>
  							</div>
                        </c:if>
                </div>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="mdl-card mdl-shadow--6dp center-block" style="margin-left: 10px;">
            <div class="row">
                <div class="col-md-3 col-sm-4" style="display:flex;align-items:center;"><img class="img-circle img-responsive" src="${param.logoUrl}"></div>
                <div class="col-md-9 col-sm-8">
                    <div class="mdl-card__title">${param.name}</div>
                    <div class="mdl-card__supporting-text"> 

                        <p><strong>Precio </strong> ${param.price} MXN<br/>
                            <strong>Creado en </strong> ${param.creationDate}
                        </p>
                    </div>
                    <c:if test="${param.combo == true}">
                        	<div class="mdl-card__menu">
    							<a href="<c:url value="product/${param.id}/edit"/>" class="mdl-button mdl-js-button mdl-button--icon">
    								<i class="material-icons">edit</i>
    							</a>
  							</div>
                        </c:if>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>
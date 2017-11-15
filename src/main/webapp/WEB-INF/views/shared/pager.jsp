<%-- 
    Document   : pager
    Created on : Sep 21, 2015, 5:30:47 PM
    Author     : Alberto Rubalcaba
--%>

<ul class="pager">
    <c:set var="previous" scope="request" value="?page_number=${param.pageNumber - 1}"/>
    <c:set var="next" scope="request" value="?page_number=${param.pageNumber + 1}"/>
    <%-- Create previous button --%>
    <c:choose>
        <c:when test="${param.pageNumber == 1}">
            <%-- No page 0 --%>
            <li id="disabledPrevious" class="disabled"><a href="#">Previous</a></li>
            <div class="mdl-tooltip" for="disabledPrevious">There is no page 0</div>
        </c:when>
        <c:otherwise>
            <li><a href="${previous}">Previous</a></li>
        </c:otherwise>
    </c:choose>

    <%-- create next button --%>
    <c:choose>
        <c:when test="${param.pageNumber == param.pagesAvailable}">
            <%-- no more pages --%>
            <li id="disabledNext" class="disabled"><a href="#">Next</a></li>
            <div class="mdl-tooltip" for="disabledNext">No more pages</div>
        </c:when>
        <c:otherwise>
            <li><a href="${next}">Next</a></li>
        </c:otherwise>
    </c:choose>
</ul>
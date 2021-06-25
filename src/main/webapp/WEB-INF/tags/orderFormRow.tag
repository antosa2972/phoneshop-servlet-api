<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order" %>
<%@attribute name="errors" required="true" type="java.util.Map" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tr>
    <td>${label}<span class="red">*</span></td>
    <c:set var="error" value="${errors[name]}"/>
    <c:choose>
        <c:when test="${label.equals('Phone')}">
            <td><input type="tel" pattern="37529[0-9]{3}[0-9]{2}[0-9]{2}" name="${name}" value="${not empty error ? param[name]: order[name]}">
                <c:if test="${not empty error}">
                    <div class="error">
                            ${error}
                    </div>
                </c:if>
            </td>
        </c:when>
        <c:otherwise>
            <td><input name="${name}" value="${not empty error ? param[name]: order[name]}">
                <c:if test="${not empty error}">
                    <div class="error">
                            ${error}
                    </div>
                </c:if>
            </td>
        </c:otherwise>
    </c:choose>
</tr>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:price_history pageTitle="Price History">
    <h2>
        Product ${product.description}
    </h2>
    <table>
        <thead>
        <td>
            Date
        </td>
        <td>
            Price
        </td>
        </thead>
        <tr>
            <c:forEach var="priceHistory" items="${product.history}">
            <td>${priceHistory.date}</td>
            <td><fmt:formatNumber value="${priceHistory.price}" type="currency"
                                  currencySymbol="${product.currency.symbol}"/>
            </td>
        </tr>
        </c:forEach>
    </table>
</tags:price_history>